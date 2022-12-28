package sirs.stardrive.services

import com.google.common.io.BaseEncoding
import com.google.common.primitives.Longs
import org.springframework.stereotype.Service
import sirs.stardrive.config.ErrorMessage
import sirs.stardrive.config.StarDriveException
import sirs.stardrive.models.InitOtpDto
import sirs.stardrive.models.NewOtpDto
import sirs.stardrive.models.Otp
import sirs.stardrive.models.OtpRepository
import java.security.SecureRandom
import java.time.Instant
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and
import kotlin.math.pow

@Service
class OtpService(private val otpRepository: OtpRepository) {

    val keyType = "AES"
    val hmacAlgorithm = "HmacSHA1"

    fun getOtp(username: String) =
        otpRepository.findByUsername(username) ?: throw StarDriveException(ErrorMessage.OTP_ALREADY_EXISTS)

    fun hasOtp(username: String) = otpRepository.findByUsername(username) != null

    fun genSecret(): SecretKeySpec {
        val seed = ByteArray(128 / 8)
        SecureRandom().nextBytes(seed)
        return SecretKeySpec(seed, keyType)
    }

    // the minus 0 is temporary
    fun computeEpoch() = (Instant.now().epochSecond - 0) / 30 // 30 second epoch

    fun computeHmac(key: SecretKeySpec, counter: Long, length: Int): Int {
        val mac = Mac.getInstance(hmacAlgorithm)
        mac.init(key)
        val hmac = mac.doFinal(Longs.toByteArray(counter))
        val offset = hmac[hmac.size - 1] and 0xF
        var trunc = 0;
        for (i in 0..3) {
            (trunc shl 8).also { trunc = it }
            trunc = trunc or (hmac[offset + i].toInt() and 0xff)
        }
        // 2 ** 31 - 1 (31 bits = 1)
        trunc = trunc and 0x7FFFFFFF
        trunc = trunc.mod(10.0.pow(length.toDouble()).toInt())
        return trunc
    }

    @Throws(StarDriveException::class)
    fun verifyTotp(username: String, guess: Int): Boolean {
        val otpUser = getOtp(username)
        val counter = computeEpoch()
        for (i in -1..1) {
            if (guess == computeHmac(
                    SecretKeySpec(BaseEncoding.base32().decode(otpUser.secret), keyType), counter + i, 6
                )
            ) {
                return true
            }
        }
        return false
    }

    @Throws(StarDriveException::class)
    fun initOtp(dto: NewOtpDto): InitOtpDto {
        val username = dto.username
        if (hasOtp(username)) throw StarDriveException(ErrorMessage.OTP_ALREADY_EXISTS)
        return InitOtpDto(
            otpRepository.save(
                Otp(username, BaseEncoding.base32().encode(genSecret().encoded))
            )
        )
    }
}
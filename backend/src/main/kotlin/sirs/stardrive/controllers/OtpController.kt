package sirs.stardrive.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import sirs.stardrive.models.InitOtpDto
import sirs.stardrive.models.NewOtpDto
import sirs.stardrive.models.OtpTestDto
import sirs.stardrive.models.VerifyOtpDto
import sirs.stardrive.services.OtpService

@RestController
class OtpController(private val otpService: OtpService) {

    @PostMapping("/otp")
    fun initOtp(@RequestBody newOtpDto: NewOtpDto) : InitOtpDto {
        return otpService.initOtp(newOtpDto)
    }

    @PostMapping("/otp/verify")
    fun verifyOtp(@RequestBody verifyOtpDto: VerifyOtpDto): OtpTestDto {
        return OtpTestDto(otpService.verifyTotp(verifyOtpDto.username, verifyOtpDto.password))
    }
}
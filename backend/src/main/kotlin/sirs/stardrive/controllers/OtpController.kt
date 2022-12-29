package sirs.stardrive.controllers

import org.springframework.web.bind.annotation.RestController
import sirs.stardrive.services.OtpService

@RestController
class OtpController(private val otpService: OtpService) {

    // @PostMapping("/otp")
    // fun initOtp(@RequestBody newOtpDto: NewOtpDto) : InitOtpDto {
    //     return otpService.initOtp(newOtpDto)
    // }

    // @PostMapping("/otp/verify")
    // fun verifyOtp(@RequestBody verifyOtpDto: VerifyOtpDto): OtpTestDto {
    //     return OtpTestDto(otpService.verifyTotp(verifyOtpDto.username, verifyOtpDto.password))
    // }
}
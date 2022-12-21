package sirs.stardrive.controllers

import org.springframework.web.bind.annotation.*
import sirs.stardrive.models.NewPartDto
import sirs.stardrive.models.UpdatePartQuantityDto
import sirs.stardrive.services.PartService

@RestController
class PartController(private val partService: PartService) {
    @GetMapping("/parts")
    fun getParts() = partService.getParts()

    @PatchMapping("/parts/{ref}")
    fun updatePartQuantity(@PathVariable ref: Int, @RequestBody newQuantity: UpdatePartQuantityDto) =
        partService.updatePartQuantity(ref, newQuantity.quantity)

    @PostMapping("/parts")
    fun createPart(@RequestBody newPartDto: NewPartDto) = partService.createPart(newPartDto)
}
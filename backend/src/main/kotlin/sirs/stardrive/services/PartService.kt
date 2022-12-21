package sirs.stardrive.services

import org.springframework.stereotype.Service
import sirs.stardrive.config.ErrorMessage
import sirs.stardrive.config.StarDriveException
import sirs.stardrive.models.NewPartDto
import sirs.stardrive.models.Part
import sirs.stardrive.models.PartDto
import sirs.stardrive.models.PartRepository

@Service
class PartService(private val partRepository: PartRepository) {
    fun getParts() = partRepository.findAll().map { PartDto(it) }

    fun updatePartQuantity(ref: Int, quantity: Int) : PartDto {
        val part = partRepository.findByRef(ref) ?: throw StarDriveException(ErrorMessage.PART_NOT_FOUND)
        part.quantity += quantity
        return PartDto(partRepository.save(part))
    }

    fun createPart(newPartDto: NewPartDto) : PartDto {
        return try {
            PartDto(partRepository.save(Part(newPartDto)))
        } catch (e: Exception) {
            throw StarDriveException(ErrorMessage.PART_ALREADY_EXISTS)
        }
    }
}
package com.example.demo.service

import com.example.demo.domain.Template
import com.example.demo.repository.TemplateRepository
import jakarta.transaction.Transactional

import org.springframework.stereotype.Service

@Service
class TemplateService(
    private val templateRepository: TemplateRepository
) {
    @Transactional
    fun findByChannelType(channelType: String): List<Template> {
        return templateRepository.findByChannelType(channelType)
    }
}
package com.example.demo.repository

import com.example.demo.domain.Template
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TemplateRepository : JpaRepository<Template, Long> {
    fun findByChannelType(channelType: String): List<Template>
}
package com.christendijoux.watch.data

import androidx.annotation.StringRes
import com.christendijoux.watch.R

/** Contenu du portfolio, repris de christendijoux.com (src/data/customProjects.ts). */
object Portfolio {
    const val NAME = "Christen Dijoux"
    const val SITE = "https://christendijoux.com"
    const val EMAIL = "christen.dijoux@gmail.com"
    const val PHONE = "+14183762612"
    const val LINKEDIN = "https://www.linkedin.com/in/christen-dijoux"
    const val GITHUB = "https://github.com/Akiyo974"
    const val CITY = "Saguenay"

    /** Distance orthodromique Saint-Denis (La Réunion) → Saguenay, comme sur le site. */
    const val JOURNEY_KM = 14_420

    val projects = listOf(
        Project(
            id = "akiworkspace",
            name = "AkiWorkspace",
            year = "2026",
            status = Status.InProgress,
            summary = R.string.project_akiworkspace_summary,
            tags = listOf("Next.js", "Prisma", "TypeScript"),
        ),
        Project(
            id = "ctreq",
            name = "CTREQ",
            year = "2025",
            status = Status.InProgress,
            summary = R.string.project_ctreq_summary,
            tags = listOf("Next.js", "GraphQL", "WordPress"),
        ),
        Project(
            id = "ctreq-blog-archive",
            name = "CTREQ Archive",
            year = "2025",
            status = Status.Completed,
            summary = R.string.project_ctreq_archive_summary,
            tags = listOf("React", "GraphQL"),
        ),
        Project(
            id = "innovations-numeriques-fp",
            name = "Innovations FP",
            year = "2024",
            status = Status.Completed,
            summary = R.string.project_innovations_fp_summary,
            tags = listOf("React", "TypeScript"),
        ),
        Project(
            id = "webana",
            name = "Webana",
            year = "2024",
            status = Status.Completed,
            summary = R.string.project_webana_summary,
            tags = listOf("React", "Tailwind"),
        ),
        Project(
            id = "akiproject",
            name = "AkiProject",
            year = "2023",
            status = Status.Completed,
            summary = R.string.project_akiproject_summary,
            tags = listOf("PHP", "MySQL"),
        ),
        Project(
            id = "akimusic",
            name = "AkiMusic",
            year = "2023",
            status = Status.Archived,
            summary = R.string.project_akimusic_summary,
            tags = listOf("PHP", "Bootstrap"),
        ),
    )

    val experiences = listOf(
        Experience(company = "Gagnon Frères", role = R.string.role_gagnon, year = "2026"),
        Experience(company = "Synaptia Web", role = R.string.role_synaptia, year = "2025"),
        Experience(company = "CTREQ", role = R.string.role_ctreq, year = "2025"),
    )

    fun project(id: String?): Project? = projects.firstOrNull { it.id == id }
}

enum class Status(@StringRes val label: Int) {
    InProgress(R.string.project_status_in_progress),
    Completed(R.string.project_status_completed),
    Archived(R.string.project_status_archived),
}

data class Project(
    val id: String,
    val name: String,
    val year: String,
    val status: Status,
    @StringRes val summary: Int,
    val tags: List<String>,
) {
    /** Étude de cas complète sur le portfolio web. */
    val url: String get() = "${Portfolio.SITE}/project/$id"
}

data class Experience(val company: String, @StringRes val role: Int, val year: String)

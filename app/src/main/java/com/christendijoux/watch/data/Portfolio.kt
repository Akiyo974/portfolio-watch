package com.christendijoux.watch.data

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
            summary = "Workspace SaaS pour notes, idées, tâches et projets, avec partage granulaire.",
            tags = listOf("Next.js", "Prisma", "TypeScript"),
        ),
        Project(
            id = "ctreq",
            name = "CTREQ",
            year = "2025",
            status = Status.InProgress,
            summary = "Fusion de deux sites WordPress en une plateforme headless Next.js, cache triple et SEO avancé.",
            tags = listOf("Next.js", "GraphQL", "WordPress"),
        ),
        Project(
            id = "ctreq-blog-archive",
            name = "CTREQ Archive",
            year = "2025",
            status = Status.Completed,
            summary = "Archive des projets institutionnels et des articles RIRE, en React headless.",
            tags = listOf("React", "GraphQL"),
        ),
        Project(
            id = "innovations-numeriques-fp",
            name = "Innovations FP",
            year = "2024",
            status = Status.Completed,
            summary = "Vitrine des innovations numériques de plus de 40 enseignants en formation professionnelle.",
            tags = listOf("React", "TypeScript"),
        ),
        Project(
            id = "webana",
            name = "Webana",
            year = "2024",
            status = Status.Completed,
            summary = "Boutique de démonstration sur l'histoire du katana : filtres, panier et mode sombre.",
            tags = listOf("React", "Tailwind"),
        ),
        Project(
            id = "akiproject",
            name = "AkiProject",
            year = "2023",
            status = Status.Completed,
            summary = "Gestionnaire de tâches et messagerie en temps réel.",
            tags = listOf("PHP", "MySQL"),
        ),
        Project(
            id = "akimusic",
            name = "AkiMusic",
            year = "2023",
            status = Status.Archived,
            summary = "Prototype de plateforme de streaming : abonnements et playlists.",
            tags = listOf("PHP", "Bootstrap"),
        ),
    )

    val experiences = listOf(
        Experience(company = "Gagnon Frères", role = "Innovation numérique", year = "2026"),
        Experience(company = "Synaptia Web", role = "Co-fondateur", year = "2025"),
        Experience(company = "CTREQ", role = "Full-stack & DevOps", year = "2025"),
    )

    fun project(id: String?): Project? = projects.firstOrNull { it.id == id }
}

enum class Status(val label: String) {
    InProgress("En cours"),
    Completed("Terminé"),
    Archived("Archivé"),
}

data class Project(
    val id: String,
    val name: String,
    val year: String,
    val status: Status,
    val summary: String,
    val tags: List<String>,
) {
    /** Étude de cas complète sur le portfolio web. */
    val url: String get() = "${Portfolio.SITE}/project/$id"
}

data class Experience(val company: String, val role: String, val year: String)

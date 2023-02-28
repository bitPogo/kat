/*
 * Copyright (c) 2023 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package tech.antibytes.gradle.project.config.publishing

import org.gradle.api.Project
import tech.antibytes.gradle.configuration.BasePublishingConfiguration

open class ProjectPublishingConfiguration(
    project: Project,
) : BasePublishingConfiguration(project, "project") {
    val description = "PROJECT."
    val url = "https://$gitHubRepositoryPath"
    val year = 2023
}

/*
 * Copyright 2024 New Vector Ltd.
 *
 * SPDX-License-Identifier: AGPL-3.0-only OR LicenseRef-Element-Commercial
 * Please see LICENSE files in the repository root for full details.
 */

package im.vector.app.features.notifications

import im.vector.app.test.fakes.FakeNotificationUtils
import im.vector.app.test.fakes.FakeStringProvider
import im.vector.lib.strings.CommonPlurals
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test

class SummaryGroupMessageCreatorTest {

    private val stringProvider = FakeStringProvider()
    private val notificationUtils = FakeNotificationUtils()

    private val creator = SummaryGroupMessageCreator(
            stringProvider.instance,
            notificationUtils.instance,
    )

    @Test
    fun `summary title uses total notification count`() {
        val expectedNotification = notificationUtils.givenBuildSummaryListNotification()
        val roomMeta = RoomNotification.Message.Meta(
                summaryLine = "room",
                messageCount = 2,
                latestTimestamp = 123L,
                roomId = "room-id",
                shouldBing = false
        )
        val simpleMeta = OneShotNotification.Append.Meta(
                key = "simple",
                summaryLine = "simple",
                isNoisy = false,
                timestamp = 456L,
        )

        val result = creator.createSummaryNotification(
                roomNotifications = listOf(roomMeta),
                invitationNotifications = emptyList(),
                simpleNotifications = listOf(simpleMeta),
                useCompleteNotificationFormat = true,
        )

        val expectedNbEvents = 3
        val expectedTitle = "test-${CommonPlurals.notification_compat_summary_title}-$expectedNbEvents"

        notificationUtils.verifyBuildSummaryListNotificationCalledWith(expectedTitle)
        result shouldBeEqualTo expectedNotification
    }
}


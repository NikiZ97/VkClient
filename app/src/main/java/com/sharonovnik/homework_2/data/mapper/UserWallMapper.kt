package com.sharonovnik.homework_2.data.mapper

import com.sharonovnik.homework_2.data.local.entity.PostEntity
import com.sharonovnik.homework_2.data.network.response.UserWallAttachment
import com.sharonovnik.homework_2.data.network.response.UserWallResponse
import com.sharonovnik.homework_2.domain.model.Attachment
import com.sharonovnik.homework_2.domain.model.Photo

class UserWallMapper : Mapper<UserWallResponse, List<PostEntity>> {
    override fun map(value: UserWallResponse): List<PostEntity> {
        val result = mutableListOf<PostEntity>()
        val profiles = value.profiles
        value.wallItem.forEach {
            val entity = PostEntity(
                it.id, it.fromId, it.date, mapAttachments(it.attachments), it.text,
                it.likes, it.isFavorite, it.ownerId, it.comments, true
            )
            val profile = profiles.find { profile -> profile.id == it.ownerId }
            profile?.let { p ->
                entity.publicName = p.firstName + " " + p.last_name
                entity.avatar = p.photo
            }
            result += entity
        }
        return result
    }

    private fun mapAttachments(atts: List<UserWallAttachment>?): List<Attachment?>? {
        return atts?.map { mapAttachment(it) }
    }

    private fun mapAttachment(attachment: UserWallAttachment): Attachment? {
        if (attachment.photo == null) {
            return null
        }
        return Attachment(with(attachment.photo) {
            Photo(albumId, date, id, ownerId, hasTags, accesskey, sizes, postId, text, userId, width)
        })
    }
}
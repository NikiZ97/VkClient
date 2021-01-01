package com.sharonovnik.vkclient.data.mapper

import com.sharonovnik.vkclient.data.local.entity.GroupEntity
import com.sharonovnik.vkclient.data.local.entity.PostEntity
import com.sharonovnik.vkclient.data.network.response.NewsFeedResponse
import com.sharonovnik.vkclient.domain.model.Group
import com.sharonovnik.vkclient.domain.model.Post
import kotlin.math.absoluteValue

class PostsDtoToEntityMapper : Mapper<NewsFeedResponse, Pair<List<PostEntity>, List<GroupEntity>>> {

    override fun map(value: NewsFeedResponse): Pair<List<PostEntity>, List<GroupEntity>> {
        val posts = mapPostsDtoToEntity(value.posts)
        val groups = mapGroupsDtoToEntity(value.groups)
        posts.forEach { post ->
            val group = groups.find { it.id == post.source }
            group?.let {
                post.publicName = it.name
                post.avatar = it.photo50
            }
        }
        return Pair(posts, groups)
    }

    private fun mapPostsDtoToEntity(posts: List<Post>): List<PostEntity> {
        return posts.map { mapPost(it) }
    }

    private fun mapPost(post: Post): PostEntity {
        return PostEntity(
            post.postId, post.sourceId.absoluteValue, post.date.toLong(), post.attachments, post.text, post.likes,
            post.isFavorite, post.ownerId, post.comments
        )
    }

    private fun mapGroupsDtoToEntity(groups: List<Group>): List<GroupEntity> {
        return groups.map { mapGroup(it) }
    }

    private fun mapGroup(group: Group): GroupEntity {
        return GroupEntity(
            group.id.absoluteValue, group.name, group.screenName, group.photo50
        )
    }
}

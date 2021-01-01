package com.sharonovnik.homework_2.data.network

import com.sharonovnik.homework_2.data.network.response.*
import io.reactivex.Single
import retrofit2.http.*

interface ApiService {
    @GET("newsfeed.get?filters=post&count=50&v=5.124")
    fun getNewsFeed(@Query("user_id") appId: String): Single<VkResponse<NewsFeedResponse>>

    @POST("likes.{method_name}?v=5.124&type=post")
    fun addOrRemovePostLike(
        @Path("method_name") methodName: String, @Query("item_id") postId: Int,
        @Query("owner_id") ownerId: Long
    ): Single<VkResponse<LikesResponse>>

    @GET("wall.getComments?need_likes=1&v=5.126&extended=1&sort=desc")
    fun getPostComments(
        @Query("owner_id") ownerId: Long, @Query("post_id") postId: Int,
        @Query("count") portion: Int
    ): Single<VkResponse<CommentsResponse>>

    @POST("wall.createComment?v=5.126")
    @FormUrlEncoded
    fun createComment(
        @Field("owner_id") ownerId: Long, @Field("post_id") postId: Int,
        @Field("message") message: String
    ): Single<VkResponse<CreateCommentResponse>>

    @GET("wall.getComment?v=5.126&extended=1")
    fun getComment(
        @Query("owner_id") ownerId: Long, @Query("comment_id") commentId: Int
    ): Single<VkResponse<CommentsResponse>>

    @GET("users.get?v=5.126")
    fun getCurrentUserInfo(@Query("fields") fields: String): Single<VkArrayResponse<UserInfoResponse>>

    @GET("wall.get?v=5.126&extended=1")
    fun getCurrentUserWall(
        @Query("filter") filter: String = "owner"
    ): Single<VkResponse<UserWallResponse>>

    @POST("wall.post?v=5.126")
    @FormUrlEncoded
    fun composePost(@Field("message") text: String): Single<VkResponse<ComposePostResponse>>

    @GET("wall.getById?v=5.126&extended=1") // posts = "userID_postID". Eg, "123_456"
    fun getPostById(@Query("posts") posts: String): Single<VkResponse<UserWallResponse>>
}
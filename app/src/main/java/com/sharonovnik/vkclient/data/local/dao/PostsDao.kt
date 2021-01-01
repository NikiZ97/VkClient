package com.sharonovnik.vkclient.data.local.dao

import androidx.room.*
import com.sharonovnik.vkclient.data.CurrentUser
import com.sharonovnik.vkclient.data.local.PostWithGroup
import com.sharonovnik.vkclient.data.local.entity.GroupEntity
import com.sharonovnik.vkclient.data.local.entity.PostEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface PostsDao {
    @Transaction
    fun insertPostsAndGroups(posts: List<PostEntity>, groups: List<GroupEntity>) {
        clearGroups()
        insertPosts(posts)
        insertGroups(groups)
    }

    @Transaction
    fun updateUserWall(posts: List<PostEntity>) {
        clearUserPosts(CurrentUser.userId)
        insertPosts(posts)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPosts(posts: List<PostEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPost(post: PostEntity): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGroups(groups: List<GroupEntity>)

    @Query("SELECT * FROM ${PostEntity.TABLE_NAME} WHERE is_user_wall_post = 0 ")
    fun getPosts(): Single<List<PostWithGroup>>

    @Query("SELECT * FROM ${PostEntity.TABLE_NAME} WHERE is_user_wall_post = 0")
    fun getFavoritePosts(): Single<List<PostWithGroup>>

    @Query("SELECT * FROM ${PostEntity.TABLE_NAME} WHERE source_id = :currentUserId")
    fun getPostsWithoutGroups(currentUserId: Long): Single<List<PostEntity>>

    @Query("SELECT * FROM ${PostEntity.TABLE_NAME} WHERE id = :postId ORDER BY date DESC")
    fun getPostById(postId: Int): Single<PostWithGroup>

    @Update
    fun updatePost(post: PostEntity): Single<Int>

    @Query("UPDATE ${PostEntity.TABLE_NAME} SET id = :postId WHERE id = -1")
    fun updatePost(postId: Int): Single<Int>

    @Query("DELETE FROM ${PostEntity.TABLE_NAME}")
    fun clearPosts()

    @Query("DELETE FROM ${PostEntity.TABLE_NAME} WHERE source_id = :userId")
    fun clearUserPosts(userId: Long)

    @Query("DELETE FROM ${GroupEntity.TABLE_NAME}")
    fun clearGroups()
}
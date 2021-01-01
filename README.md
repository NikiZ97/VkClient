# VkClient
VkClient is a mobile app that shows a newsfeed with all related functional

You can easily view the latest news with the ability to react, view comments and leave yours. 

The app contains:
* Latest newsfeed for your account 
* Your personal profile info including your wall
* Ability to view comments to posts and leave yours
* Ability to save posts images on a device and share them 
* Ability to save a post to favorites list

## Stack

The app is written entirely in Kotlin, it uses [RxRedux](https://github.com/freeletics/RxRedux) library to  implement Redux pattern using RxJava. 
It uses some kind of [Clean Architecture](https://github.com/android10/Android-CleanArchitecture) to make the code more structured and easy to extend. 
Dagger 2 is used for DI and [AAC MVVM](https://developer.android.com/jetpack/guide) settles in presetation layer.

//package app

import app.videoPlayer
import kotlinx.coroutines.*
import kotlinx.css.*
import react.*
import react.dom.*
import styled.css
import styled.styledDiv
import styled.styledH1
import styled.styledTable
import kotlin.browser.window

interface AppState : RState {
    var currentVideo: Video?
    var unwatchedVideos:List<Video>
    var watchedVideos:List<Video>
}


class App : RComponent<RProps, AppState>() {

    override fun AppState.init() {
        unwatchedVideos = listOf()
                /*Video(1, "Building and breaking things", "John Doe", "https://youtu.be/PsaFVLr8t4E"),
                Video(2, "The development process", "Jane Smith", "https://youtu.be/PsaFVLr8t4E"),
                Video(3, "The Web 7.0", "Matt Miller", "https://youtu.be/PsaFVLr8t4E")
        )*/
        watchedVideos = listOf()/*
                Video(4, "Mouseless development", "Tom Jerry", "https://youtu.be/PsaFVLr8t4E")
        )*/
        val mainScope = MainScope()
        mainScope.launch {
            val videos = fetchVideos()
            setState {
                unwatchedVideos = videos
            }
        }
    }


    override fun RBuilder.render() {
        styledH1 {
            css {
                fontFamily = "sans-serif"
            }
            +"KotlinConf Explorer"
            div {
                h3 {
                    +"Videos to watch"
                }
                videoList {
                    videos = state.unwatchedVideos
                    selectedVideo = state.currentVideo
                    onSelectVideo = { video ->
                        setState {
                            currentVideo = video
                        }
                    }
                }
                h3 {
                    +"Videos watched"
                }
                videoList {
                    videos = state.watchedVideos
                    selectedVideo = state.currentVideo
                    onSelectVideo = { video ->
                        setState {
                            currentVideo = video
                        }
                    }
                }
            }
            state.currentVideo?.let { currntVideo ->
                videoPlayer {
                    video = currntVideo
                    unwatchedVideo = currntVideo in state.unwatchedVideos
                    onWatchedButtonPressed = {
                        if (video in state.unwatchedVideos) {
                            setState {
                                unwatchedVideos -= video
                                watchedVideos += video
                            }
                        }
                        else
                            setState {
                                unwatchedVideos += video
                                watchedVideos -= video
                            }
                    }
                }
            }
            /*styledDiv {
                css {
                    position = Position.absolute
                    top = 10.px
                    right = 10.px
                }
                h3 {
                    +"John Doe: Building and breaking things"
                }
                img {
                    attrs {
                        src = "https://via.placeholder.com/640x360.png?text=Video+Player+Placeholder"
                    }

                }
            }*/
        }
    }

}

suspend fun fetchVideo(id:Int):Video {
    val responsePromise = window.fetch("https://my-json-server.typicode.com/kotlin-hands-on/kotlinconf-json/videos/$id")
    val response = responsePromise.await()
    val jsonPromise = response.json()
    val json = jsonPromise.await()
    return json.unsafeCast<Video>()
}

suspend fun fetchVideos ():List<Video> = coroutineScope {
    (1..25).map { id->
        async { fetchVideo(id) }
    }.awaitAll()
}
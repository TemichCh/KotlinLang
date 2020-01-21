//package app

import kotlinx.html.js.onClickFunction
import react.*
import react.dom.p

interface VideoListProps : RProps {
    var videos: List<Video>
    var selectedVideo: Video?
    var onSelectVideo: (Video) -> Unit
}

/*
interface VideoListState : RState {
    var selectedVideo: Video?
}
*/

class VideoList(props: VideoListProps) : RComponent<VideoListProps, RState>() {
    override fun RBuilder.render() {
        for (video in props.videos) {
            p {
                key = video.id.toString()
                attrs {
                    onClickFunction = {
                        //window.alert("Clicked $video!")
                        props.onSelectVideo(video)
                        /*setState {
                            selectedVideo = video
                        }*/
                    }
                }
                if (video == props.selectedVideo) {
                    +"▶ "
                }
                +"${video.speaker}: ${video.title}"
            }
        }
    }
}

fun RBuilder.videoList(handler: VideoListProps.() -> Unit): ReactElement {
    return child(VideoList::class) {
        this.attrs(handler)
    }
}
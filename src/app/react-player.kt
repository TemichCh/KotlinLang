@file:JsModule("react-player")

package app

import react.*

@JsName("default")
external val ReactPlayer: RClass<ReactPlayerProps>

external interface ReactPlayerProps:RProps {
    var url:String
}
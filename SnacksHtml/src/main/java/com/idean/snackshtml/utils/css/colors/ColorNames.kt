package com.idean.snackshtml.utils.css.colors

/**
 * Created by Mickael Calatraba on 4/8/21.
 * Copyright (c) 2021 Idean. All rights reserved.
 */
data class ColorNames(
    val colors: Map<String, String> = mapOf(
        // REDS
        "red" to "#FF0000",
        "lightsalmon" to "#FFA07A",
        "salmon" to "#FA8072",
        "darksalmon" to "#E9967A",
        "lightcoral" to "#F08080",
        "indianred" to "#CD5C5C",
        "crimson" to "#DC143C",
        "firebrick" to "#B22222",
        "darkred" to "#8B0000",

        // ORANGE
        "coral" to "#FF7F50",
        "tomato" to "#FF6347",
        "orangered" to "#FF4500",
        "gold" to "#FFD700",
        "orange" to "#FFA500",
        "darkorange" to "#FF8C00",

        // YELLOW
        "yellow" to "#FFFF00",
        "lightyellow" to "#FFFFE0",
        "lemonchiffon" to "#FFFACD",
        "lightgoldenrodyellow" to "#FAFAD2",
        "papayawhip" to "#FFEFD5",
        "moccasin" to "#FFE4B5",
        "peachpuff" to "#FFDAB9",
        "palegoldenrod" to "#EEE8AA",
        "khaki" to "#F0E68C",
        "darkkhaki" to "#BDB76B",

        // GREEN
        "lime" to "#00FF00",
        "awngreen" to "#7CFC00",
        "chartreuse" to "#7FFF00",
        "limegreen" to "#32CD32",
        "forestgreen" to "#228B22",
        "green" to "#008000",
        "darkgreen" to "#006400",
        "greenyellow" to "#ADFF2F",
        "yellowgreen" to "#9ACD32",
        "springgreen" to "#00FF7F",
        "mediumspringgreen" to "#00FA9A",
        "lightgreen" to "#90EE90",
        "palegreen" to "#98FB98",
        "darkseagreen" to "#8FBC8F",
        "mediumseagreen" to "#3CB371",
        "seagreen" to "#2E8B57",
        "olive" to "#808000",
        "darkolivegreen" to "#556B2F",
        "olivedrab" to "#6B8E23",

        // CYAN
        "cyan" to "#00FFFF",
        "aqua" to "#00FFFF",
        "lightcyan" to "#E0FFFF",
        "aquamarine" to "#7FFFD4",
        "mediumaquamarine" to "#66CDAA",
        "paleturquoise" to "#AFEEEE",
        "turquoise" to "#40E0D0",
        "mediumturquoise" to "#48D1CC",
        "darkturquoise" to "#00CED1",
        "lightseagreen" to "#20B2AA",
        "cadetblue" to "#5F9EA0",
        "darkcyan" to "#008B8B",
        "teal" to "#008080",

        // BLUE
        "blue" to "#0000FF",
        "powderblue" to "#B0E0E6",
        "lightblue" to "#ADD8E6",
        "lightskyblue" to "#87CEFA",
        "skyblue" to "#87CEEB",
        "deepskyblue" to "#00BFFF",
        "lightsteelblue" to "#B0C4DE",
        "dodgerblue" to "#1E90FF",
        "cornflowerblue" to "#6495ED",
        "steelblue" to "#4682B4",
        "royalblue" to "#4169E1",
        "mediumblue" to "#0000CD",
        "darkblue" to "#00008B",
        "navy" to "#000080",
        "midnightblue" to "#191970",
        "mediumslateblue" to "#7B68EE",
        "slateblue" to "#6A5ACD",
        "darkslateblue" to "#483D8B",

        // PURPLE
        "fuchsia" to "#FF00FF",
        "magenta" to "#FF00FF",
        "lavender" to "#E6E6FA",
        "thistle" to "#D8BFD8",
        "plum" to "#DDA0DD",
        "violet" to "#EE82EE",
        "orchid" to "#DA70D6",
        "mediumorchid" to "#BA55D3",
        "mediumpurple" to "#9370DB",
        "blueviolet" to "#8A2BE2",
        "darkviolet" to "#9400D3",
        "darkorchid" to "#9932CC",
        "darkmagenta" to "#8B008B",
        "purple" to "#800080",
        "indigo" to "#4B0082",

        // PINK
        "pink" to "#FFC0CB",
        "lightpink" to "#FFB6C1",
        "hotpink" to "#FF69B4",
        "deeppink" to "#FF1493",
        "palevioletred" to "#DB7093",
        "mediumvioletred" to "#C71585",

        // WHITE
        "white" to "#FFFFFF",
        "snow" to "#FFFAFA",
        "honeydew" to "#F0FFF0",
        "mintcream" to "#F5FFFA",
        "azure" to "#F0FFFF",
        "aliceblue" to "#F0F8FF",
        "ghostwhite" to "#F8F8FF",
        "whitesmoke" to "#F5F5F5",
        "seashell" to "#FFF5EE",
        "beige" to "#F5F5DC",
        "oldlace" to "#FDF5E6",
        "floralwhite" to "#FFFAF0",
        "ivory" to "#FFFFF0",
        "antiquewhite" to "#FAEBD7",
        "linen" to "#FAF0E6",
        "lavenderblush" to "#FFF0F5",
        "mistyrose" to "#FFE4E1",

        //GRAY
        "black" to "#000000",
        "gainsboro" to "#DCDCDC",
        "lightgray" to "#D3D3D3",
        "silver" to "#C0C0C0",
        "darkgray" to "#A9A9A9",
        "gray" to "#808080",
        "dimgray" to "#696969",
        "lightslategray" to "#778899",
        "slategray" to "#708090",
        "darkslategray" to "#2F4F4F",

        // BROWN
        "cornsilk" to "#FFF8DC",
        "blanchedalmond" to "#FFEBCD",
        "bisque" to "#FFE4C4",
        "navajowhite" to "#FFDEAD",
        "wheat" to "#F5DEB3",
        "burlywood" to "#DEB887",
        "tan" to "#D2B48C",
        "rosybrown" to "#BC8F8F",
        "sandybrown" to "#F4A460",
        "goldenrod" to "#DAA520",
        "peru" to "#CD853F",
        "chocolate" to "#D2691E",
        "saddlebrown" to "#8B4513",
        "sienna" to "#A0522D",
        "brown" to "#A52A2A",
        "maroon" to "#800000"
    )
)
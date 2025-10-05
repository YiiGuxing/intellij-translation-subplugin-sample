package cn.yiiguxing.translation.subplugin.sample

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

object SampleIcons {
    @JvmField
    val icon: Icon = IconLoader.getIcon("/icon.svg", SampleIcons::class.java)
}
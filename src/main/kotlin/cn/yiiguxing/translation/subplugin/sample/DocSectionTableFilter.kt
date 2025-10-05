package cn.yiiguxing.translation.subplugin.sample

import cn.yiiguxing.plugin.translate.openapi.documentation.DocumentationElementFilter
import org.jsoup.nodes.Element

/**
 * A sample implementation of [DocumentationElementFilter] that filters out
 * and restores `<td>` elements with class `section` inside a `<table>` with class `sections`.
 */
class DocSectionTableFilter: DocumentationElementFilter {

    override fun filterElements(body: Element): List<Element> {
        // <table class="sections">
        // <td valign="top" class="section">

       val filterElements= body.select("table.sections td.section").toList()
        filterElements.forEachIndexed { index, element ->
            element.replaceWith(Element("td").attr("id", "section-$index"))
        }

        return filterElements
    }

    override fun restoreElements(body: Element, elements: List<Element>) {
        elements.forEachIndexed { index, element ->
            body.selectFirst("""td[id="section-$index"]""")?.replaceWith(element)
        }
    }

}
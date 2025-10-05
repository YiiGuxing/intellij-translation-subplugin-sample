package cn.yiiguxing.translation.subplugin.sample

import cn.yiiguxing.plugin.translate.openapi.documentation.DocumentationTranslationService
import com.intellij.codeInsight.documentation.DocumentationActionProvider
import com.intellij.lang.Language
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.EDT
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.asContextElement
import com.intellij.openapi.components.service
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.impl.EditorCssFontResolver
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.text.Strings
import com.intellij.psi.PsiDocCommentBase
import com.intellij.psi.PsiDocumentManager
import com.intellij.ui.components.JBLoadingPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.scale.JBUIScale
import com.intellij.util.ui.ExtendableHTMLViewFactory.Extensions.BLOCK_VIEW_EX
import com.intellij.util.ui.ExtendableHTMLViewFactory.Extensions.FIT_TO_WIDTH_IMAGES
import com.intellij.util.ui.ExtendableHTMLViewFactory.Extensions.INLINE_VIEW_EX
import com.intellij.util.ui.ExtendableHTMLViewFactory.Extensions.LINE_VIEW_EX
import com.intellij.util.ui.ExtendableHTMLViewFactory.Extensions.PARAGRAPH_VIEW_EX
import com.intellij.util.ui.ExtendableHTMLViewFactory.Extensions.WBR_SUPPORT
import com.intellij.util.ui.HTMLEditorKitBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JEditorPane

class DocumentationTranslationSample : DocumentationActionProvider {

    override fun additionalActions(
        editor: Editor,
        docComment: PsiDocCommentBase?,
        renderedText: String?
    ): List<AnAction> {
        if (renderedText.isNullOrEmpty()) {
            return emptyList()
        }

        val language = PsiDocumentManager.getInstance(editor.project ?: return emptyList())
            .getPsiFile(editor.document)
            ?.language
        return listOf(DocumentationTranslationSampleAction(renderedText, language))
    }

}


private class DocumentationTranslationSampleAction(private val documentation: String, private val language: Language?) :
    AnAction(
        "Translate Documentation (Sample)",
        null,
        SampleIcons.icon
    ) {
    override fun getActionUpdateThread() = ActionUpdateThread.BGT

    override fun actionPerformed(event: AnActionEvent) {
        val dialog = DocumentationDialog(event.project?:return)
        dialog.translate(documentation, language)
        dialog.show()
    }
}

private class DocumentationDialog(private val project: Project) : DialogWrapper(project) {

    private val editorPane = JEditorPane()
    private val loadingPanel = JBLoadingPanel(BorderLayout(), disposable)

    init {
        title = "Documentation"
        init()
        setSize(JBUIScale.scale(600), JBUIScale.scale(400))
    }

    override fun createCenterPanel(): JComponent {
        editorPane.editorKit = HTMLEditorKitBuilder()
            .replaceViewFactoryExtensions(
                INLINE_VIEW_EX,
                PARAGRAPH_VIEW_EX,
                LINE_VIEW_EX,
                BLOCK_VIEW_EX,
                FIT_TO_WIDTH_IMAGES,
                WBR_SUPPORT
            )
            .withFontResolver(EditorCssFontResolver.getGlobalInstance()).build()

        val scrollPane = JBScrollPane().apply { setViewportView(editorPane) }
        loadingPanel.contentPanel.add(scrollPane, BorderLayout.CENTER)

        return loadingPanel
    }

    fun translate(documentation: String, language: Language?) {
        val service = DocumentationTranslationService.getInstance()
        if (service.isTranslated(documentation)) {
            editorPane.text = documentation
            return
        }

        loadingPanel.startLoading()
        val job = project.service<SampleCoroutineScope>().launch(Dispatchers.IO) {
            val translated = try {
                service.translate(documentation, language)
            } catch (e: Throwable) {
                showError(e.message ?: "Unknown error")
                return@launch
            }

            showResult(translated)
        }

        Disposer.register(disposable) { job.cancel() }
    }

    private suspend fun showResult(result: String) {
        val modalityState = ModalityState.stateForComponent(loadingPanel)
        withContext(Dispatchers.EDT + modalityState.asContextElement()) {
            editorPane.text = result
            editorPane.caretPosition = 0
            loadingPanel.stopLoading()
        }
    }

    private suspend fun showError(message: String) {
        showResult(
            """
              <html>
                <body>
                  <p style="color: red;">Translation failed: ${Strings.escapeXmlEntities(message)}</p>
                </body>
              </html>
            """.trimIndent()
        )
    }
}
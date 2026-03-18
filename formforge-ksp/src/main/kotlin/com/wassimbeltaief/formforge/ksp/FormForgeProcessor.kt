package com.wassimbeltaief.formforge.ksp

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.ksp.writeTo
import com.wassimbeltaief.formforge.ksp.codegen.FormStateGenerator
import com.wassimbeltaief.formforge.ksp.parser.SchemaParser

internal class FormForgeProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {

    private val parser = SchemaParser(logger)
    private val stateGenerator = FormStateGenerator()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val annotated = resolver
            .getSymbolsWithAnnotation("com.wassimbeltaief.formforge.core.schema.FormSchema")
            .toList()

        val deferred = annotated.filter { !it.validate() }

        annotated
            .filter { it.validate() && it is KSClassDeclaration }
            .forEach { processClass(it as KSClassDeclaration) }

        return deferred
    }

    private fun processClass(classDecl: KSClassDeclaration) {
        val schema = parser.parse(classDecl) ?: return
        val containingFile = classDecl.containingFile ?: return
        val deps = Dependencies(aggregating = false, containingFile)

        stateGenerator.generate(schema).writeTo(codeGenerator, deps)
    }
}

package com.example.help_code.presentation.jackson

import androidx.lifecycle.ViewModel
import com.ctc.wstx.stax.WstxInputFactory
import com.ctc.wstx.stax.WstxOutputFactory
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import timber.log.Timber

class JacksonMapperViewModel : ViewModel() {


    fun parsingXML() {
        val xmlFactory = XmlFactory.builder()
            .xmlInputFactory(WstxInputFactory())
            .xmlOutputFactory(WstxOutputFactory())
            .build()

        val xmlDeserializer = XmlMapper(xmlFactory)
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


        val simpleModel = SimpleModel("x", "y")
        Timber.e(xmlDeserializer.writeValueAsString(simpleModel))

        val stringToParse = "<Test><x>1</x><y>2</y></Test>"
        val finalObject = xmlDeserializer.readValue(stringToParse, SimpleModel::class.java)
        Timber.e("parse xml: x = ${finalObject?.x}")
        Timber.e("parse xml: y = ${finalObject?.y}")

        val inner = TestInnerModel("x", "y")
        val testModel = TestModel(inner)
        Timber.e(xmlDeserializer.writeValueAsString(testModel))

        val stringToParse2 = "<TestModel><inner><x>1</x><y>2</y></inner></TestModel>"
        val finalObject2 = xmlDeserializer.readValue(stringToParse2, TestModel::class.java)
        Timber.e("parse inner xml: x = ${finalObject2?.inner?.x}")
        Timber.e("parse inner xml: y = ${finalObject2?.inner?.y}")
    }
}
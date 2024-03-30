package com.example.help_code.presentation.jackson

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import com.ctc.wstx.stax.WstxInputFactory
import com.ctc.wstx.stax.WstxOutputFactory
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.dataformat.xml.XmlFactory
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import timber.log.Timber

class JacksonMapperViewModel : ViewModel() {


         class Simple(val x: String?, val y: String?):Parcelable{
             constructor(parcel: Parcel) : this(
                 parcel.readString(),
                 parcel.readString()
             )

             override fun writeToParcel(parcel: Parcel, flags: Int) {
                 parcel.writeString(x)
                 parcel.writeString(y)
             }

             override fun describeContents(): Int {
                 return 0
             }

             companion object CREATOR : Parcelable.Creator<Simple> {
                 override fun createFromParcel(parcel: Parcel): Simple {
                     return Simple(parcel)
                 }

                 override fun newArray(size: Int): Array<Simple?> {
                     return arrayOfNulls(size)
                 }
             }

         }
    class TestModel() : Parcelable {
        //        var simple: Simple? = null
        var x: String? = null
        var y: String? = null

        constructor(parcel: Parcel) : this(parcel.readString(), parcel.readString()) {
        }

        constructor(x: String?, y: String?) : this() {
            this.x = x
            this.y = y
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {

        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<TestModel> {
            override fun createFromParcel(parcel: Parcel): TestModel {
                return TestModel(parcel)
            }

            override fun newArray(size: Int): Array<TestModel?> {
                return arrayOfNulls(size)
            }
        }
    }


    fun parsingXML() {
        val xmlFactory = XmlFactory.builder()
            .xmlInputFactory(WstxInputFactory())
            .xmlOutputFactory(WstxOutputFactory())
            .build()

        val xmlDeserializer = XmlMapper(xmlFactory)
//        val xmlDeserializer = XmlMapper(JacksonXmlModule().apply {
//            setDefaultUseWrapper(false)
//        }).registerKotlinModule()
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

//        val simple = Simple("x", "y")
        val testModel =
            TestModel("x", "y")
        Timber.e(xmlDeserializer.writeValueAsString(testModel))

        val stringToParse1 = "<Test><Simple><x>1</x><y>2</y></Simple></Test>"
        val stringToParse = "<Test><x>1</x><y>2</y></Test>"
        val finalObject = xmlDeserializer.readValue(stringToParse, TestModel::class.java)
        Timber.e("parse xml: all = ${finalObject.toString()}")
        Timber.e("parse xml: x = ${finalObject?.x}")
        Timber.e("parse xml: y = ${finalObject?.y}")
    }
}
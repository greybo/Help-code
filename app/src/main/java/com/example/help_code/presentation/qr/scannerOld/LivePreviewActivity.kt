/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.help_code.presentation.qr.scannerOld

//import com.google.android.gms.vision.CameraSource
//import com.google.mlkit.common.model.LocalModel
//import com.google.mlkit.vision.demo.CameraSource
//import com.google.mlkit.vision.demo.CameraSourcePreview
//import com.google.mlkit.vision.demo.GraphicOverlay
//import com.google.mlkit.vision.demo.kotlin.barcodescanner.BarcodeScannerProcessor
//import com.google.mlkit.vision.demo.kotlin.facedetector.FaceDetectorProcessor
//import com.google.mlkit.vision.demo.kotlin.facemeshdetector.FaceMeshDetectorProcessor
//import com.google.mlkit.vision.demo.kotlin.labeldetector.LabelDetectorProcessor
//import com.google.mlkit.vision.demo.kotlin.objectdetector.ObjectDetectorProcessor
//import com.google.mlkit.vision.demo.kotlin.posedetector.PoseDetectorProcessor
//import com.google.mlkit.vision.demo.kotlin.segmenter.SegmenterProcessor
//import com.google.mlkit.vision.demo.kotlin.textdetector.TextRecognitionProcessor
//import com.google.mlkit.vision.demo.preference.PreferenceUtils
//import com.google.mlkit.vision.demo.preference.SettingsActivity
//import com.google.mlkit.vision.demo.preference.SettingsActivity.LaunchSource
//import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
//import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
//import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
//import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions
//import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
//import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
//import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import android.widget.Spinner
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.example.help_code.R
import com.example.help_code.presentation.qr.scannerOld.barcode.BarcodeScannerProcessor
import com.example.help_code.presentation.qr.scannerOld.util.CameraSource
import com.example.help_code.presentation.qr.scannerOld.util.CameraSourcePreview
import com.example.help_code.presentation.qr.scannerOld.util.GraphicOverlay
import com.example.help_code.presentation.qr.scannerOld.util.PreferenceUtils
import com.google.android.gms.common.annotation.KeepName
import com.google.mlkit.vision.barcode.ZoomSuggestionOptions.ZoomCallback
import java.io.IOException

/** Live preview demo for ML Kit APIs. */
@KeepName
class LivePreviewActivity :
  AppCompatActivity(), OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

  private var cameraSource: CameraSource? = null
  private var preview: CameraSourcePreview? = null
  private var graphicOverlay: GraphicOverlay? = null
//  private var selectedModel = OBJECT_DETECTION
  private var selectedModel = BARCODE_SCANNING

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Log.d(TAG, "onCreate")
    setContentView(R.layout.activity_vision_live_preview)

    preview = findViewById(R.id.preview_view)
    if (preview == null) {
      Log.d(TAG, "Preview is null")
    }

    graphicOverlay = findViewById(R.id.graphic_overlay)
    if (graphicOverlay == null) {
      Log.d(TAG, "graphicOverlay is null")
    }

    val spinner = findViewById<Spinner>(R.id.spinner)
    val options: MutableList<String> = ArrayList()
//    options.add(OBJECT_DETECTION)
//    options.add(OBJECT_DETECTION_CUSTOM)
//    options.add(CUSTOM_AUTOML_OBJECT_DETECTION)
//    options.add(FACE_DETECTION)
    options.add(BARCODE_SCANNING)
//    options.add(IMAGE_LABELING)
//    options.add(IMAGE_LABELING_CUSTOM)
//    options.add(CUSTOM_AUTOML_LABELING)
//    options.add(POSE_DETECTION)
//    options.add(SELFIE_SEGMENTATION)
//    options.add(TEXT_RECOGNITION_LATIN)
//    options.add(TEXT_RECOGNITION_CHINESE)
//    options.add(TEXT_RECOGNITION_DEVANAGARI)
//    options.add(TEXT_RECOGNITION_JAPANESE)
//    options.add(TEXT_RECOGNITION_KOREAN)
//    options.add(FACE_MESH_DETECTION)

    // Creating adapter for spinner
    val dataAdapter = ArrayAdapter(this, R.layout.spinner_style, options)

    // Drop down layout style - list view with radio button
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    // attaching data adapter to spinner
    spinner.adapter = dataAdapter
    spinner.onItemSelectedListener = this

    val facingSwitch = findViewById<ToggleButton>(R.id.facing_switch)
    facingSwitch.setOnCheckedChangeListener(this)

//    val settingsButton = findViewById<ImageView>(R.id.settings_button)
//    settingsButton.setOnClickListener {
//      val intent = Intent(applicationContext, SettingsActivity::class.java)
//      intent.putExtra(SettingsActivity.EXTRA_LAUNCH_SOURCE, LaunchSource.LIVE_PREVIEW)
//      startActivity(intent)
//    }

    createCameraSource(selectedModel)
  }

  @Synchronized
  override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
    // An item was selected. You can retrieve the selected item using
    // parent.getItemAtPosition(pos)
    selectedModel = parent?.getItemAtPosition(pos).toString()
    Log.d(TAG, "Selected model: $selectedModel")
    preview?.stop()
    createCameraSource(selectedModel)
    startCameraSource()
  }

  override fun onNothingSelected(parent: AdapterView<*>?) {
    // Do nothing.
  }

  override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
    Log.d(TAG, "Set facing")
    if (cameraSource != null) {
      if (isChecked) {
        cameraSource?.setFacing(CameraSource.CAMERA_FACING_FRONT)
      } else {
        cameraSource?.setFacing(CameraSource.CAMERA_FACING_BACK)
      }
    }
    preview?.stop()
    startCameraSource()
  }

  private fun createCameraSource(model: String) {
    // If there's no existing cameraSource, create one.
    if (cameraSource == null) {
      cameraSource =
        CameraSource(
          this,
          graphicOverlay
        )
    }
    try {
      when (model) {
        BARCODE_SCANNING -> {
          Log.i(TAG, "Using Barcode Detector Processor")
          var zoomCallback: ZoomCallback? = null
          if (PreferenceUtils.shouldEnableAutoZoom(this)) {
            zoomCallback = ZoomCallback { zoomLevel: Float -> cameraSource!!.setZoom(zoomLevel) }
          }
          cameraSource!!.setMachineLearningFrameProcessor(
            BarcodeScannerProcessor(this, zoomCallback)
          )
        }
        else -> Log.e(TAG, "Unknown model: $model")
      }
    } catch (e: Exception) {
      Log.e(TAG, "Can not create image processor: $model", e)
      Toast.makeText(
          applicationContext,
          "Can not create image processor: " + e.message,
          Toast.LENGTH_LONG
        )
        .show()
    }
  }

  /**
   * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
   * (e.g., because onResume was called before the camera source was created), this will be called
   * again when the camera source is created.
   */
  private fun startCameraSource() {
    if (cameraSource != null) {
      try {
        if (preview == null) {
          Log.d(TAG, "resume: Preview is null")
        }
        if (graphicOverlay == null) {
          Log.d(TAG, "resume: graphOverlay is null")
        }
        preview!!.start(cameraSource, graphicOverlay)
      } catch (e: IOException) {
        Log.e(TAG, "Unable to start camera source.", e)
        cameraSource!!.release()
        cameraSource = null
      }
    }
  }

  public override fun onResume() {
    super.onResume()
    Log.d(TAG, "onResume")
    createCameraSource(selectedModel)
    startCameraSource()
  }

  /** Stops the camera. */
  override fun onPause() {
    super.onPause()
    preview?.stop()
  }

  public override fun onDestroy() {
    super.onDestroy()
    if (cameraSource != null) {
      cameraSource?.release()
    }
  }

  companion object {
    private const val BARCODE_SCANNING = "Barcode Scanning"


//    private const val OBJECT_DETECTION = "Object Detection"
//    private const val OBJECT_DETECTION_CUSTOM = "Custom Object Detection"
//    private const val CUSTOM_AUTOML_OBJECT_DETECTION = "Custom AutoML Object Detection (Flower)"
//    private const val FACE_DETECTION = "Face Detection"
//    private const val TEXT_RECOGNITION_LATIN = "Text Recognition Latin"
//    private const val TEXT_RECOGNITION_CHINESE = "Text Recognition Chinese"
//    private const val TEXT_RECOGNITION_DEVANAGARI = "Text Recognition Devanagari"
//    private const val TEXT_RECOGNITION_JAPANESE = "Text Recognition Japanese"
//    private const val TEXT_RECOGNITION_KOREAN = "Text Recognition Korean"
//    private const val IMAGE_LABELING = "Image Labeling"
//    private const val IMAGE_LABELING_CUSTOM = "Custom Image Labeling (Birds)"
//    private const val CUSTOM_AUTOML_LABELING = "Custom AutoML Image Labeling (Flower)"
//    private const val POSE_DETECTION = "Pose Detection"
//    private const val SELFIE_SEGMENTATION = "Selfie Segmentation"
//    private const val FACE_MESH_DETECTION = "Face Mesh Detection (Beta)"

    private const val TAG = "LivePreviewActivity"
  }
}

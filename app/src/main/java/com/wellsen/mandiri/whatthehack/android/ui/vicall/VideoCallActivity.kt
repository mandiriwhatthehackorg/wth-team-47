/*
 * *
 *  * Created by Wellsen on 7/20/19 6:18 PM
 *  * for Mandiri What The Hack Hackathon
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 7/20/19 6:08 PM
 *
 */

package com.wellsen.mandiri.whatthehack.android.ui.vicall

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log.getStackTraceString
import android.view.SurfaceView
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.wellsen.mandiri.whatthehack.android.R
import com.wellsen.mandiri.whatthehack.android.ui.main.MainActivity
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration
import timber.log.Timber

class VideoCallActivity : AppCompatActivity() {

  private var mRtcEngine: RtcEngine? = null

  // Permissions
  private val PERMISSION_REQ_ID = 22
  private val REQUESTED_PERMISSIONS =
    arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)

  private val LOG_TAG = MainActivity::class.java.simpleName

  // Handle SDK Events
  private val mRtcEventHandler = object : IRtcEngineEventHandler() {
    override fun onFirstRemoteVideoDecoded(uid: Int, width: Int, height: Int, elapsed: Int) {
      runOnUiThread {
        // set first remote user to the main bg video container
        setupRemoteVideoStream(uid)
      }
    }

    // remote user has left channel
    override fun onUserOffline(uid: Int, reason: Int) { // Tutorial Step 7
      runOnUiThread { onRemoteUserLeft() }
    }

    // remote user has toggled their video
    override fun onUserMuteVideo(uid: Int, toggle: Boolean) { // Tutorial Step 10
      runOnUiThread { onRemoteUserVideoToggle(uid, toggle) }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_video_call)

    if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) && checkSelfPermission(
        REQUESTED_PERMISSIONS[1],
        PERMISSION_REQ_ID
      )
    ) {
      initAgoraEngine()
    }

    findViewById<AppCompatImageView>(R.id.audioBtn).visibility =
      View.GONE // set the audio button hidden
    findViewById<AppCompatImageView>(R.id.leaveBtn).visibility =
      View.GONE // set the leave button hidden
    findViewById<AppCompatImageView>(R.id.videoBtn).visibility =
      View.GONE // set the video button hidden
  }

  private fun initAgoraEngine() {
    try {
      mRtcEngine = RtcEngine.create(baseContext, getString(R.string.agora_app_id), mRtcEventHandler)
    } catch (e: Exception) {
      Timber.e(getStackTraceString(e))

      throw RuntimeException("NEED TO check rtc sdk init fatal error\n" + getStackTraceString(e))
    }

    setupSession()
  }

  private fun setupSession() {
    mRtcEngine?.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION)

    mRtcEngine?.enableVideo()

    mRtcEngine?.setVideoEncoderConfiguration(
      VideoEncoderConfiguration(
        VideoEncoderConfiguration.VD_1280x720,
        VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_30,
        VideoEncoderConfiguration.STANDARD_BITRATE,
        VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
      )
    )
  }

  private fun setupLocalVideoFeed() {

    // setup the container for the local user
    val videoContainer = findViewById<FrameLayout>(R.id.floating_video_container)
    val videoSurface = RtcEngine.CreateRendererView(baseContext)
    videoSurface.setZOrderMediaOverlay(true)
    videoContainer.addView(videoSurface)
    mRtcEngine?.setupLocalVideo(VideoCanvas(videoSurface, VideoCanvas.RENDER_MODE_FIT, 0))
  }

  private fun setupRemoteVideoStream(uid: Int) {
    // setup ui element for the remote stream
    val videoContainer = findViewById<FrameLayout>(R.id.bg_video_container)
    // ignore any new streams that join the session
    if (videoContainer.childCount >= 1) {
      return
    }

    val videoSurface = RtcEngine.CreateRendererView(baseContext)
    videoContainer.addView(videoSurface)
    mRtcEngine?.setupRemoteVideo(VideoCanvas(videoSurface, VideoCanvas.RENDER_MODE_FIT, uid))
    mRtcEngine?.setRemoteSubscribeFallbackOption(Constants.STREAM_FALLBACK_OPTION_AUDIO_ONLY)

  }

  fun onAudioMuteClicked(view: View) {
    val btn = view as AppCompatImageView
    if (btn.isSelected) {
      btn.isSelected = false
      btn.setImageResource(R.drawable.audio_toggle_btn)
    } else {
      btn.isSelected = true
      btn.setImageResource(R.drawable.audio_toggle_active_btn)
    }

    mRtcEngine?.muteLocalAudioStream(btn.isSelected)
  }

  fun onVideoMuteClicked(view: View) {
    val btn = view as AppCompatImageView
    if (btn.isSelected) {
      btn.isSelected = false
      btn.setImageResource(R.drawable.video_toggle_btn)
    } else {
      btn.isSelected = true
      btn.setImageResource(R.drawable.video_toggle_active_btn)
    }

    mRtcEngine?.muteLocalVideoStream(btn.isSelected)

    val container = findViewById<FrameLayout>(R.id.floating_video_container)
    container.visibility = if (btn.isSelected) View.GONE else View.VISIBLE
    val videoSurface = container.getChildAt(0) as SurfaceView
    videoSurface.setZOrderMediaOverlay(!btn.isSelected)
    videoSurface.visibility = if (btn.isSelected) View.GONE else View.VISIBLE
  }

  // join the channel when user clicks UI button
  fun onjoinChannelClicked(view: View) {
    mRtcEngine?.joinChannel(
      null,
      "test-channel",
      "Extra Optional Data",
      0
    ) // if you do not specify the uid, Agora will assign one.
    setupLocalVideoFeed()
    findViewById<AppCompatImageView>(R.id.joinBtn).visibility =
      View.GONE // set the join button hidden
    findViewById<AppCompatImageView>(R.id.audioBtn).visibility =
      View.VISIBLE // set the audio button hidden
    findViewById<AppCompatImageView>(R.id.leaveBtn).visibility =
      View.VISIBLE // set the leave button hidden
    findViewById<AppCompatImageView>(R.id.videoBtn).visibility =
      View.VISIBLE // set the video button hidden
  }

  fun onLeaveChannelClicked(view: View) {
    leaveChannel()
    removeVideo(R.id.floating_video_container)
    removeVideo(R.id.bg_video_container)
    findViewById<AppCompatImageView>(R.id.joinBtn).visibility =
      View.VISIBLE // set the join button visible
    findViewById<AppCompatImageView>(R.id.audioBtn).visibility =
      View.GONE // set the audio button hidden
    findViewById<AppCompatImageView>(R.id.leaveBtn).visibility =
      View.GONE // set the leave button hidden
    findViewById<AppCompatImageView>(R.id.videoBtn).visibility =
      View.GONE // set the video button hidden

    setResult(Activity.RESULT_OK)
    finish()
  }

  private fun leaveChannel() {
    mRtcEngine?.leaveChannel()
  }

  private fun removeVideo(containerID: Int) {
    val videoContainer = findViewById<FrameLayout>(containerID)
    videoContainer.removeAllViews()
  }

  private fun onRemoteUserVideoToggle(uid: Int, toggle: Boolean) {
    val videoContainer = findViewById<FrameLayout>(R.id.bg_video_container)

    val videoSurface = videoContainer.getChildAt(0) as SurfaceView
    videoSurface.visibility = if (toggle) View.GONE else View.VISIBLE

    // add an icon to let the other user know remote video has been disabled
    if (toggle) {
      val noCamera = AppCompatImageView(this)
      noCamera.setImageResource(R.drawable.video_disabled)
      videoContainer.addView(noCamera)
    } else {
      val noCamera = videoContainer.getChildAt(1) as AppCompatImageView
      if (noCamera != null) {
        videoContainer.removeView(noCamera)
      }
    }
  }

  private fun onRemoteUserLeft() {
    removeVideo(R.id.bg_video_container)
  }

  fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
    Timber.i(LOG_TAG, "checkSelfPermission $permission $requestCode")
    if (ContextCompat.checkSelfPermission(
        this,
        permission
      ) != PackageManager.PERMISSION_GRANTED
    ) {

      ActivityCompat.requestPermissions(
        this,
        REQUESTED_PERMISSIONS,
        requestCode
      )
      return false
    }
    return true
  }

}
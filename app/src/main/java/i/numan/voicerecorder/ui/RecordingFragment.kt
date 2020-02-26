package i.numan.voicerecorder.ui


import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.media.MediaRecorder
import android.media.MediaRecorder.*
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.View.OnClickListener
import androidx.core.app.ActivityCompat.checkSelfPermission
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import i.numan.voicerecorder.R
import i.numan.voicerecorder.R.drawable
import i.numan.voicerecorder.R.layout
import i.numan.voicerecorder.R.string.*
import kotlinx.android.synthetic.main.fragment_recording.*
import java.text.SimpleDateFormat
import java.util.*


const val REQUEST_CODE = 1011

class RecordingFragment : Fragment(layout.fragment_recording) {

    lateinit var navController: NavController
    var isRecoding: Boolean = false
    val recordPermission = android.Manifest.permission.RECORD_AUDIO
    lateinit var mediaRecorder: MediaRecorder
    var FILE_NAME: String = ""
    var PATH_NAME: String = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view) //now navController is instantiated

        initOnClickListeners()
    }

    private fun initOnClickListeners() {
        record_list_btn.setOnClickListener(onClickListener)
        record_btn.setOnClickListener(onClickListener)
    }


    private val onClickListener: OnClickListener = OnClickListener { view ->
        when (view.id) {
            R.id.record_list_btn -> {
                navController.navigate(R.id.action_recordingFragment_to_recordingListFragment2)
            }
            R.id.record_btn -> {

                recordSetting()
            }
        }
    }


    private fun recordSetting() {
        if (isRecoding) {
            // Recording Stop
            stopRecording()
            record_btn.setImageDrawable(this@RecordingFragment.resources.getDrawable(drawable.record_btn_stopped))
            isRecoding = false
        } else {
            if (checkPermission()) {
                // Recording Start
                startRecording()
                record_btn.setImageDrawable(this@RecordingFragment.resources.getDrawable(drawable.record_btn_recording))
                isRecoding = true
            }
        }
    }

    private fun startRecording() {
        // Chronometer
        record_timer.apply {
            // base will prevent the timer running in background
            base = SystemClock.elapsedRealtime()
            start()
        }
        //absolute path will convert the path to string
         PATH_NAME = activity!!.getExternalFilesDir("/")!!.absolutePath
        val formatter = SimpleDateFormat(
            getString(formating_pattern),
            Locale.getDefault()
        )
        val currentTime = Date()
        val dateString = formatter.format(currentTime)
        FILE_NAME = "${"Recording"}${dateString}${".3gp"}"
        record_filename.text = "${getString(recording_started_with_file_name)}${" "}$FILE_NAME"

        mediaRecorder = MediaRecorder()
            mediaRecorder.apply {
                setAudioSource(AudioSource.MIC);
                setOutputFormat(OutputFormat.THREE_GPP);
                setAudioEncoder(AudioEncoder.AMR_NB);
                setOutputFile("$PATH_NAME/$FILE_NAME");
                prepare();
                start();
            }


    }

    private fun stopRecording() {
        record_timer.stop()
        record_filename.text = "${getString(recording_stopped_with_file_saved)}${" "}$PATH_NAME"

            mediaRecorder.apply {
                stop()
                release()
        }
    }

    private fun checkPermission(): Boolean {
        if (checkSelfPermission(context!!, recordPermission) ==
            PERMISSION_GRANTED
        ) {
            return true
        } else {
            requestPermissions(
                activity!!,
                arrayOf(recordPermission),
                REQUEST_CODE
            )
            return false
        }

    }
}

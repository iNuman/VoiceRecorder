package i.numan.voicerecorder.ui


import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import i.numan.voicerecorder.R
import i.numan.voicerecorder.R.drawable
import i.numan.voicerecorder.R.drawable.player_pause_btn
import i.numan.voicerecorder.adapter.AudioListAdapter
import i.numan.voicerecorder.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_recording_list.*
import kotlinx.android.synthetic.main.player_sheet.*
import java.io.File


class RecordingListFragment : Fragment(R.layout.fragment_recording_list),
    AudioListAdapter.Interaction {


    override fun onItemSelected(position: Int, item: File) {

        println("ffnet: Playing ${item.name}")
        fileToPlay = item
        if (isPlaying) {
//            Allready Playing we'll stop the audio
            stopAudio()
            playAudio(fileToPlay = item)
            isPlaying = false
        } else {
//            We'll play the audio
            //fileToPlay = item
            playAudio(fileToPlay = item)
            isPlaying = true
        }
    }


    private fun playAudio(fileToPlay: File) {
        val bottomSheetBehavior = BottomSheetBehavior.from(player_sheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        mediaPlayer = MediaPlayer()
        mediaPlayer.apply {
            setDataSource(fileToPlay.absolutePath)
            prepareAsync()
            start()
        }
        player_header_title.text = "Playing"
        player_file_name_tv.text = fileToPlay.name
        player_play_btn.setImageDrawable(activity!!.resources.getDrawable(player_pause_btn))

// it'll be called after the track finishes
        mediaPlayer.setOnCompletionListener {
            stopAudio()
            player_header_title.text = "Finished"
        }

        isPlaying = true
    }

    private fun stopAudio() {
        player_header_title.text = "Stopped"
        player_file_name_tv.text = fileToPlay.name
        player_play_btn.setImageDrawable(activity!!.resources.getDrawable(drawable.player_play_btn))
        isPlaying = false
    }


    var recordingPath = ""
    lateinit var mediaPlayer: MediaPlayer
    var isPlaying: Boolean = false
    lateinit var fileToPlay: File
    lateinit var audioListAdapter: AudioListAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        supportingMethods()
        handlingBottomSheetState()

    }

    private fun handlingBottomSheetState() {
        val bottomSheetBehavior = BottomSheetBehavior.from(player_sheet)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                // React to state change
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    }
                }

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // React to dragging events
            }
        })
    }

    private fun supportingMethods() {
        //absolute path will convert the path to string
        val RECORDINGS_PATH = activity!!.getExternalFilesDir("/")!!.absolutePath
        val directory = File(RECORDINGS_PATH)
//        All Files will be listed here from storage
        val allFiles = directory.listFiles()

        initRecyclerView(allFiles!!)

    }

    private fun initRecyclerView(allFiles: Array<File>) {
        audio_list_view.apply {
            layoutManager = LinearLayoutManager(activity)
            removeItemDecoration(TopSpacingItemDecoration(padding = 30))
            addItemDecoration(TopSpacingItemDecoration(padding = 30))
            audioListAdapter = AudioListAdapter(
                interaction = this@RecordingListFragment,
                allFiles = allFiles
            )
            // audioListAdapter.setData(allFiles)
            adapter = audioListAdapter
        }
    }


}

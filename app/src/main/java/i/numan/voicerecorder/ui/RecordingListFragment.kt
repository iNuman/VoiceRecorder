package i.numan.voicerecorder.ui


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import i.numan.voicerecorder.R
import i.numan.voicerecorder.adapter.AudioListAdapter
import i.numan.voicerecorder.util.TopSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_recording_list.*
import kotlinx.android.synthetic.main.player_sheet.*
import java.io.File


class RecordingListFragment : Fragment(R.layout.fragment_recording_list),
    AudioListAdapter.Interaction {


    override fun onItemSelected(position: Int, item: File) {

        println("ffnet: Playing ${item.name}")
    }


    var recordingPath = ""
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
//        All Files will be list here
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

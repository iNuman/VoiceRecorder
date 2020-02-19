package i.numan.voicerecorder.ui


import android.os.Bundle
import android.view.View
import android.view.View.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import i.numan.voicerecorder.R
import kotlinx.android.synthetic.main.fragment_recording.*


class RecordingFragment : Fragment(R.layout.fragment_recording) {

    lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view) //now navController is instantiated

        initOnClickListeners()
    }

    private fun initOnClickListeners() {
        record_list_btn.setOnClickListener(onClickListener)
    }


    private val onClickListener: OnClickListener = OnClickListener { view ->
        when (view.id) {
            R.id.record_list_btn -> {
                navController.navigate(R.id.action_recordingFragment_to_recordingListFragment2)
            }

        }
    }
}

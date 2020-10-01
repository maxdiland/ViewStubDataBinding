package viewstub.databinding

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingConversion
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import viewstub.databinding.databinding.ActivityMainBinding

@BindingConversion
fun fromBool(isVisible: Boolean): Int {
    return if (isVisible) View.VISIBLE else View.GONE
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil
            .setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.viewModel = ViewModel()
        binding.lifecycleOwner = this
    }
}

class ViewModel {
    val state = MutableLiveData<State>().apply { value = State.Loading }
    val retryClickListener = View.OnClickListener { loadData() }
    private var counter: Int = 0

    init {
        loadData()
    }

    fun loadData() {
        state.value = State.Loading
        Handler(Looper.getMainLooper())
            .postDelayed(
                {
                    when (counter) {
                        0 -> state.value = State.Error
                        1 -> state.value = State.Empty
                        else -> state.value = State.Done
                    }
                    counter++
                }, 3000
            )
    }

    sealed class State {
        object Loading : State()
        object Empty : State()
        object Done : State()
        object Error : State()
    }
}
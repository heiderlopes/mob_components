package br.com.heiderlopes.mobcomponents.loadingbutton

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import br.com.heiderlopes.mobcomponents.R
import br.com.heiderlopes.mobcomponents.databinding.LoadingButtonBinding

class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var title: String? = null
    private var loadingTitle: String? = null

    private val binding = LoadingButtonBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private var state: LoadingButtonState = LoadingButtonState.Normal
        set(value) {
            field = value
            refreshState()
        }

    private fun refreshState() {
        isEnabled = state.isEnabled
        isClickable = state.isEnabled
        refreshDrawableState()

        binding.tvTitleProgressButton.run {
            isEnabled = state.isEnabled
            isClickable = state.isEnabled
        }

        binding.pbProgressButton.visibility = state.progressVisibility

        when(state) {
            LoadingButtonState.Normal -> {
                binding.tvTitleProgressButton.text = title
            }
            LoadingButtonState.Loading -> {
                binding.tvTitleProgressButton.text = loadingTitle
            }
        }

    }

    init {
        setLayout(attrs)
        refreshState()
    }

    fun setLoading(loading: Boolean = false) {
        state = if(loading) {
            LoadingButtonState.Loading
        } else {
            LoadingButtonState.Normal
        }
    }

    private fun setLayout(attrs: AttributeSet?) {
        attrs?.let {
            val attributes = context.obtainStyledAttributes(
                it,
                R.styleable.LoadingButton
            )

            setBackgroundResource(R.drawable.loading_button_background)

            val titleResId =
                attributes.getResourceId(R.styleable.LoadingButton_loading_button_title, 0)
            if (titleResId != 0) title = context.getString(titleResId)


            val loadingTitleResId =
                attributes.getResourceId(R.styleable.LoadingButton_loading_button_loading_title, 0)
            if (loadingTitleResId != 0) loadingTitle = context.getString(loadingTitleResId)

            attributes.recycle()
        }
    }

    sealed class LoadingButtonState(val isEnabled: Boolean, val progressVisibility: Int) {
        object Normal : LoadingButtonState(true, View.GONE)
        object Loading : LoadingButtonState(false, View.VISIBLE)
    }
}

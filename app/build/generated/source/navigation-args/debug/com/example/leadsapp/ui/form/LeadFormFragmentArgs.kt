package com.example.leadsapp.ui.form

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.Int
import kotlin.jvm.JvmStatic

public data class LeadFormFragmentArgs(
  public val leadId: Int = -1,
) : NavArgs {
  public fun toBundle(): Bundle {
    val result = Bundle()
    result.putInt("leadId", this.leadId)
    return result
  }

  public fun toSavedStateHandle(): SavedStateHandle {
    val result = SavedStateHandle()
    result.set("leadId", this.leadId)
    return result
  }

  public companion object {
    @JvmStatic
    public fun fromBundle(bundle: Bundle): LeadFormFragmentArgs {
      bundle.setClassLoader(LeadFormFragmentArgs::class.java.classLoader)
      val __leadId : Int
      if (bundle.containsKey("leadId")) {
        __leadId = bundle.getInt("leadId")
      } else {
        __leadId = -1
      }
      return LeadFormFragmentArgs(__leadId)
    }

    @JvmStatic
    public fun fromSavedStateHandle(savedStateHandle: SavedStateHandle): LeadFormFragmentArgs {
      val __leadId : Int?
      if (savedStateHandle.contains("leadId")) {
        __leadId = savedStateHandle["leadId"]
        if (__leadId == null) {
          throw IllegalArgumentException("Argument \"leadId\" of type integer does not support null values")
        }
      } else {
        __leadId = -1
      }
      return LeadFormFragmentArgs(__leadId)
    }
  }
}

package com.example.leadsapp.ui.list

import android.os.Bundle
import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import com.example.leadsapp.R
import kotlin.Int

public class LeadListFragmentDirections private constructor() {
  private data class ActionLeadListFragmentToLeadFormFragment(
    public val leadId: Int = -1,
  ) : NavDirections {
    public override val actionId: Int = R.id.action_leadListFragment_to_leadFormFragment

    public override val arguments: Bundle
      get() {
        val result = Bundle()
        result.putInt("leadId", this.leadId)
        return result
      }
  }

  public companion object {
    public fun actionLeadListFragmentToLeadFormFragment(leadId: Int = -1): NavDirections =
        ActionLeadListFragmentToLeadFormFragment(leadId)

    public fun actionLeadListFragmentToLogsFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_leadListFragment_to_logsFragment)
  }
}

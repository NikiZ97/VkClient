package com.sharonovnik.homework_2.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sharonovnik.homework_2.R
import kotlinx.android.synthetic.main.dialog_fragment_main_user_info.*

class UserInfoBottomSheetDialog: BottomSheetDialogFragment() {
    private lateinit var items: List<UserInfoItem>
    private lateinit var adapter: UserInfoAdapter

    companion object {
        private const val EXTRA_INFO_ITEMS = "extra_info_items"

        fun newInstance(items: List<UserInfoItem>): UserInfoBottomSheetDialog {
            val bundle = Bundle().apply {
                putParcelableArrayList(EXTRA_INFO_ITEMS, items as ArrayList<UserInfoItem>)
            }
            return UserInfoBottomSheetDialog().apply {
                arguments = bundle
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        items = arguments?.getParcelableArrayList<UserInfoItem>(EXTRA_INFO_ITEMS) as ArrayList<UserInfoItem>
        return inflater.inflate(R.layout.dialog_fragment_main_user_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = UserInfoAdapter()
        infoItems.layoutManager = LinearLayoutManager(requireContext())
        infoItems.adapter = adapter
        adapter.items = items
    }
}
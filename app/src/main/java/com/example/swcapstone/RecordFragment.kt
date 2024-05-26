package com.example.swcapstone

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class RecordFragment : Fragment() {

    private lateinit var nicknameTextView: TextView
    private lateinit var nicknameEditText: EditText

    companion object {
        private const val PREFS_NAME = "com.example.swcapstone.prefs"
        private const val KEY_NICKNAME = "nickname"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_record, container, false)

        nicknameTextView = view.findViewById(R.id.nickname)
        nicknameEditText = view.findViewById(R.id.nickname_edit)

        val sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedNickname = sharedPreferences.getString(KEY_NICKNAME, "사용자님")
        nicknameTextView.text = savedNickname
        nicknameEditText.setText(savedNickname)

        nicknameTextView.setOnClickListener {
            nicknameTextView.visibility = View.GONE
            nicknameEditText.visibility = View.VISIBLE
            nicknameEditText.requestFocus()
        }

        nicknameEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                val newNickname = nicknameEditText.text.toString()
                if (TextUtils.isEmpty(newNickname) || newNickname.length < 3) {
                    Toast.makeText(requireContext(), "닉네임은 최소 3글자 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
                } else {
                    nicknameTextView.text = newNickname
                    nicknameEditText.visibility = View.GONE
                    nicknameTextView.visibility = View.VISIBLE


                    sharedPreferences.edit().putString(KEY_NICKNAME, newNickname).apply()


                    Toast.makeText(requireContext(), "닉네임이 변경되었습니다!", Toast.LENGTH_SHORT).show()
                }
                true
            } else {
                false
            }
        }

        return view
    }
}
package com.example.contactmanagment.fragments.sms

import android.os.Bundle
import android.telephony.SmsManager
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.contactmanagment.R
import com.example.contactmanagment.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_s_m_s.*
import kotlinx.android.synthetic.main.fragment_s_m_s.view.*

class SMSFragment : Fragment() {

    private val args by navArgs<SMSFragmentArgs>()
    private lateinit var mUserViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_s_m_s, container, false)

        mUserViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        view.editPhoneNumber.setText(args.activeUser.teleNum.toString())


        view.sendMessageButton.setOnClickListener {

            val phoneNumber = editPhoneNumber.text.toString()
            val message = editMessage.text.toString()

            if (phoneNumber == "" || message == "") {
                Toast.makeText(requireContext(), "Field cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                if (TextUtils.isDigitsOnly(phoneNumber)) {
                    val smsManager: SmsManager = SmsManager.getDefault()

                    smsManager.sendTextMessage("876 $phoneNumber", null, message, null, null)
                    Toast.makeText(requireContext(), "Message Sent", Toast.LENGTH_SHORT).show()
                    editMessage.text.clear()
                } else {
                    Toast.makeText(requireContext(), "Please enter the correct number", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        return view
    }

}


package com.example.contactmanagment.fragments.contact

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.contactmanagment.R
import com.example.contactmanagment.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_contact.view.*
import kotlinx.android.synthetic.main.fragment_update.*
import kotlinx.android.synthetic.main.fragment_update.view.textFirstName
import kotlinx.android.synthetic.main.fragment_update.view.textLastName
import kotlinx.android.synthetic.main.fragment_update.view.textTelephoneNum


class ContactFragment : Fragment() {

    private val args by navArgs<ContactFragmentArgs>()

    private lateinit var mUserViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        mUserViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        view.textFirstName.setText(args.allTheUser.firstName)
        view.textLastName.setText(args.allTheUser.lastName)
        view.textTelephoneNum.setText(args.allTheUser.teleNum.toString())

        view.floatingCallButton.setOnClickListener {
            phoneCall()
        }

        view.floatingMessageButton.setOnClickListener {
            textMessage()
        }

        view.floatingUpdateButton.setOnClickListener {
            val action = ContactFragmentDirections.actionContactFragmentToUpdateFragment(currentUser  = args.allTheUser)
            findNavController().navigate(action)
        }

        //Add menu
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.delete_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                if (menuItem.itemId == R.id.menu_delete) {
                    deleteUser()
                }
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return view
    }

    private fun deleteUser() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){ _, _ ->
             mUserViewModel.deleteUser(args.allTheUser)
            Toast.makeText(
                requireContext(),
                "${args.allTheUser.firstName} was successfully removed.",
                Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_contactFragment_to_listFragment)
        }

        builder.setNegativeButton("No"){ _, _ -> }
        builder.setTitle("Delete ${args.allTheUser.firstName}")
        builder.setMessage("Are you sure you want to delete ${args.allTheUser.firstName}?")
        builder.create().show()
    }

    private fun checkCallPermission() {
        //Check for Permission
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CALL_PHONE), 101)//check for permission
        }
    }

    private fun checkMessagePermission() {
        //Check for Permission
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.SEND_SMS), 101)//check for permission
        }
    }

    private fun phoneCall() {
        checkCallPermission()

        val phoneNumber = Integer.parseInt(textTelephoneNum.text.toString().trim())

        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel: $phoneNumber")
        startActivity(callIntent)
    }

    private fun textMessage() {
        checkMessagePermission()

        val action = ContactFragmentDirections.actionContactFragmentToSMSFragment(activeUser = args.allTheUser)
        findNavController().navigate(action)

    }
}
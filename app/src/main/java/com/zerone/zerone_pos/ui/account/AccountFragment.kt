package com.zerone.zerone_pos.ui.account
/**
name : rafie
website : wwww.zerone-bjm.com
dibuat : april 2020
 */

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.bumptech.glide.Glide
import com.zerone.zerone_pos.R
import com.zerone.zerone_pos.database.AppDatabase
import com.zerone.zerone_pos.helpers.Const
import com.zerone.zerone_pos.helpers.SharedPrefControl
import com.zerone.zerone_pos.helpers.ShoppingCart
import com.zerone.zerone_pos.ui.login.LoginActivity
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : Fragment() {

    private lateinit var vm: AccountViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        vm = ViewModelProvider(this).get(AccountViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_account, container, false)
        Paper.init(activity!!.applicationContext)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        usernameTextView.setText(SharedPrefControl.getInstance(activity!!.applicationContext).getString(Const.USER_NAMA))
        editTextEmail.setText(SharedPrefControl.getInstance(activity!!.applicationContext).getString(Const.USER_EMAIL))

        val image = SharedPrefControl.getInstance(activity!!.applicationContext).getString(Const.USER_IMAGE)
        Glide.with(activity!!.applicationContext).load(image).into(profileCircleImageView)

        txtShop.setOnClickListener(View.OnClickListener {
            startActivity(Intent(activity!!.applicationContext, EditMerchantActivity::class.java))
        })

        txtEditProfile.setOnClickListener(View.OnClickListener {
            startActivity(Intent(activity!!.applicationContext, EditProfileActivity::class.java))
        })

        txtChangePassword.setOnClickListener(View.OnClickListener {
            startActivity(Intent(activity!!.applicationContext, ChangePasswordActivity::class.java))
        })

        txtLogout.setOnClickListener(View.OnClickListener {
            var db: AppDatabase
            db = Room.databaseBuilder(
                activity!!.applicationContext,
                AppDatabase::class.java, "zerone-pos-v3"
            ).allowMainThreadQueries().build()

            db.produkDao().deleteAll()
            db.transactionDao().deleteAll()
            ShoppingCart.removeAll(activity!!.applicationContext)
            SharedPrefControl.getInstance(activity!!.applicationContext).sLogin(false)
            SharedPrefControl.getInstance(activity!!.applicationContext).saveToken("-")
            startActivity(Intent(activity!!.applicationContext, LoginActivity::class.java))
            activity!!.finish()
        })
    }
}

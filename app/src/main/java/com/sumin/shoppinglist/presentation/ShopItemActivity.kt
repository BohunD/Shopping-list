package com.sumin.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.sumin.shoppinglist.R
import com.sumin.shoppinglist.data.ShopListRepositoryImpl
import com.sumin.shoppinglist.domain.AddShopItemUseCase
import com.sumin.shoppinglist.presentation.ShopItemViewModel
import com.sumin.shoppinglist.domain.ShopItem

class ShopItemActivity: AppCompatActivity() {
//    private lateinit var viewModel: ShopItemViewModel
//
//    private lateinit var tilName: TextInputLayout
//    private lateinit var tilCount: TextInputLayout
//    private lateinit var etName: EditText
//    private lateinit var etCount: EditText
//    private lateinit var btnSave: Button
//
    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
//        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
//        initViews()
//        addTextChangeListener()
        launchRightMode()
//        observeViewModel()
    }

    private fun launchRightMode(){
        val fragment = when(screenMode){
            MODE_EDIT -> ShopItemFragment.newInstanceEditItem(shopItemId)
            MODE_ADD -> ShopItemFragment.newInstanceAddItem()
            else-> throw RuntimeException("Unknown screen mode")
        }
        supportFragmentManager.beginTransaction().add(R.id.shop_item_container, fragment).commit()
    }
//    private fun observeViewModel(){
//        viewModel.errorInputCount.observe(this){
//            val message = if(it){
//                "Enter count!"
//            }else{
//                null
//            }
//            tilCount.error = message
//        }
//        viewModel.errorInputName.observe(this){
//            val message = if(it){
//                "Enter name!"
//            }else{
//                null
//            }
//            tilName.error = message
//        }
//        viewModel.shouldCloseScreen.observe(this){
//            finish()
//        }
//    }
//    private fun addTextChangeListener(){
//        etName.addTextChangedListener(object: TextWatcher{
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                viewModel.resetErrorInputName()
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//            }
//
//        })
//        etCount.addTextChangedListener(object: TextWatcher{
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                viewModel.resetErrorInputCount()
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//            }
//
//        })
//    }
//    private fun launchAddMode(){
//
//        btnSave.setOnClickListener{
//            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
//        }
//    }
//
//    private fun launchEditMode(){
//        viewModel.getShopItem(shopItemId)
//        viewModel.shopItem.observe(this){
//            etName.setText(it.name)
//            etCount.setText(it.count.toString())
//        }
//        btnSave.setOnClickListener{
//            viewModel.editShopItem(etName.text?.toString(), etCount.text?.toString())
//        }
//    }
//
    private fun parseIntent(){
        if(!intent.hasExtra(EXTRA_SCREEN_MODE)){
            throw RuntimeException("Param Screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if(mode != MODE_ADD&& mode!= MODE_EDIT)
            throw RuntimeException("Unknown mode $mode")
        screenMode = mode
        if(screenMode == MODE_EDIT ) {
            if(!intent.hasExtra(EXTRA_SHOP_ITEM_ID))
                throw RuntimeException("ShopItemId is absent")
            shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID,ShopItem.UNDEFINED_ID)
        }

    }
//
//    private fun initViews(){
//        tilName = findViewById(R.id.til_name)
//        tilCount = findViewById(R.id.til_count)
//        etName = findViewById(R.id.et_name)
//        etCount = findViewById(R.id.et_count)
//        btnSave = findViewById(R.id.btn_save)
//    }
//
//
//
    companion object{
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }
        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }
    }
}
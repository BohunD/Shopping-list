package com.sumin.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.sumin.shoppinglist.R
import com.sumin.shoppinglist.domain.ShopItem

class ShopItemFragment: Fragment() {
      private lateinit var viewModel: ShopItemViewModel

    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: EditText
    private lateinit var etCount: EditText
    private lateinit var btnSave: Button
    private var screenMode = MODE_UNKNOWN
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_item, container,false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        initViews(view)
        addTextChangeListener()
        launchRightMode()
        observeViewModel()
    }


        private fun launchRightMode(){
        when(screenMode){
            MODE_EDIT -> launchEditMode()
            MODE_ADD -> launchAddMode()
        }
    }
    private fun observeViewModel(){
        viewModel.errorInputCount.observe(viewLifecycleOwner){
            val message = if(it){
                "Enter count!"
            }else{
                null
            }
            tilCount.error = message
        }
        viewModel.errorInputName.observe(viewLifecycleOwner){
            val message = if(it){
                "Enter name!"
            }else{
                null
            }
            tilName.error = message
        }
        viewModel.shouldCloseScreen.observe(viewLifecycleOwner){
            activity?.onBackPressed()
        }
    }
    private fun addTextChangeListener(){
        etName.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        etCount.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }
    private fun launchAddMode(){

        btnSave.setOnClickListener{
            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    private fun launchEditMode(){
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(viewLifecycleOwner){
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }
        btnSave.setOnClickListener{
            viewModel.editShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    private fun parseParams(){
        val args = requireArguments()
        if(!args.containsKey(SCREEN_MODE)){
            throw RuntimeException("Param Screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if(mode != MODE_ADD && mode!= MODE_EDIT)
            throw RuntimeException("Unknown mode $mode")
        screenMode = mode
        if(screenMode == MODE_EDIT) {
            if(!args.containsKey(EXTRA_SHOP_ITEM_ID))
                throw RuntimeException("ShopItemId is absent")
            shopItemId = args.getInt(EXTRA_SHOP_ITEM_ID,ShopItem.UNDEFINED_ID)
        }



    }

    private fun initViews(view:View){
        tilName = view.findViewById(R.id.til_name)
        tilCount = view.findViewById(R.id.til_count)
        etName = view.findViewById(R.id.et_name)
        etCount = view.findViewById(R.id.et_count)
        btnSave = view.findViewById(R.id.btn_save)
    }



    companion object{
        private const val SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val MODE_UNKNOWN = ""

        fun newInstanceAddItem(): ShopItemFragment{

            return ShopItemFragment().apply {
                arguments = Bundle().apply{
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }

        }
        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment{
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(EXTRA_SHOP_ITEM_ID,shopItemId)
                }
            }
        }

       /* fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }
        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }*/
    }
}
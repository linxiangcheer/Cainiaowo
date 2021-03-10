package com.cniao5.mine

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/*
* koin的mine module
* */
val moduleMine = module {

    viewModel { MineViewModel() }
}
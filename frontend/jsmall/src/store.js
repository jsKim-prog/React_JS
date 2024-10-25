/* (Redux toolkit용) Store 객체 생성 : configureStore */

import { configureStore } from "@reduxjs/toolkit";
import loginSlice from './slices/loginSlice';

export default configureStore({
    reducer:{
        "loginSlice": loginSlice
    }
})
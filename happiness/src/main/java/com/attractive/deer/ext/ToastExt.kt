package com.attractive.deer.ext

// val Context.layoutInflater: LayoutInflater get() = this.layoutInflater

/*
fun Activity.showToastFinish(@IdRes lytRoot: Int, @LayoutRes lytToast: Int) {
    val customView = LayoutInflater.from(this).inflate(lytToast, findViewById(lytRoot), false)
    val binding = DataBindingUtil.bind<ToastFinishBinding>(customView)
    Toast(this).apply {
        // setGravity(Gravity.BOTTOM or Gravity.CENTER,0,0)
        duration = Toast.LENGTH_SHORT
        view = binding?.root
        show()
    }
}
fun Activity.showToastLike(@IdRes lytRoot: Int, @LayoutRes lytToast: Int, likeFlag:Boolean?) {
    val customView = LayoutInflater.from(this).inflate(lytToast, findViewById(lytRoot), false)
    val binding = DataBindingUtil.bind<ToastLikeBinding>(customView)?.apply { likeFlag?.let{flag = !likeFlag }}
    Toast(this).apply {
        setGravity(Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL,0,0)
        duration = Toast.LENGTH_SHORT
        view = binding?.root
        show()
    }
}

fun Activity.showToastHashTagOver(@IdRes lytRoot: Int, @LayoutRes lytToast: Int) {
    val customView = LayoutInflater.from(this).inflate(lytToast, findViewById(lytRoot), false)
    val binding = DataBindingUtil.bind<ToastHashTagOverBinding>(customView)
    Toast(this).apply {
        // setGravity(Gravity.BOTTOM or Gravity.CENTER,0,0)
        duration = Toast.LENGTH_SHORT
        view = binding?.root
        show()
    }
}
*/

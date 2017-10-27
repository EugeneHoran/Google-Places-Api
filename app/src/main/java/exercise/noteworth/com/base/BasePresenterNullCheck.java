package exercise.noteworth.com.base;


import android.util.Log;

public abstract class BasePresenterNullCheck<T> {
    private T mView;
    private T mNullView;

    public BasePresenterNullCheck() {
        mNullView = createNullView();
    }

    public void onAttachView(T view) {
        this.mView = view;
    }

    public void onDetachView() {
        this.mView = null;
    }

    public abstract T createNullView();

    public T getView() {
        if (mView == null) {
            Log.e(BasePresenterNullCheck.class.getSimpleName(), "getView: view is null, returning dummy view.");
            return mNullView;
        } else {
            return mView;
        }
    }
}

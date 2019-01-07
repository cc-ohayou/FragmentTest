package space.cc.com.fragmenttest.appbar;


import com.google.android.material.R;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;

import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.AttrRes;
import androidx.annotation.RestrictTo;
import androidx.annotation.StyleRes;
import androidx.annotation.StyleableRes;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.TintTypedArray;

import static androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * Utility methods to check Theme compatibility with components.
 *
 * @hide
 */
@RestrictTo(LIBRARY_GROUP)
public final class ThemeEnforcement {

    private static final int[] APPCOMPAT_CHECK_ATTRS = {R.attr.colorPrimary};
    private static final String APPCOMPAT_THEME_NAME = "Theme.AppCompat";

    private static final int[] MATERIAL_CHECK_ATTRS = {R.attr.colorPrimaryVariant};
    private static final String MATERIAL_THEME_NAME = "Theme.MaterialComponents";

    private static final int[] ANDROID_THEME_OVERLAY_ATTRS =
            new int[] {android.R.attr.theme, R.attr.theme};
    private static final int[] MATERIAL_THEME_OVERLAY_ATTR = new int[] {R.attr.materialThemeOverlay};

    private ThemeEnforcement() {}

    /**
     * Safely retrieve styled attribute information in this Context's theme, after checking whether
     * the theme is compatible with the component's given style.
     *
     * <p>Set a component's {@link R.attr#enforceMaterialTheme enforceMaterialTheme} attribute to
     * <code>true</code> to ensure that the Context's theme inherits from {@link
     * R.style#Theme_MaterialComponents Theme.MaterialComponents}. For example, you'll want to do this
     * if the component uses a new attribute defined in <code>Theme.MaterialComponents</code> like
     * {@link R.attr#colorSecondary colorSecondary}.
     *
     * <p>If {@link R.attr#enforceTextAppearance} attribute is set to <code>true</code> and
     * textAppearanceResIndices parameter is specified and has non-negative values, this will also
     * check that a valid TextAppearance is set on this component for the text appearance resources
     * passed in.
     */
    public static TypedArray obtainStyledAttributes(
            Context context,
            AttributeSet set,
            @StyleableRes int[] attrs,
            @AttrRes int defStyleAttr,
            @StyleRes int defStyleRes,
            @StyleableRes int... textAppearanceResIndices) {

        // First, check for a compatible theme.
        checkCompatibleTheme(context, set, defStyleAttr, defStyleRes);

        // Then, check that a textAppearance is set if enforceTextAppearance attribute is true
        checkTextAppearance(context, set, attrs, defStyleAttr, defStyleRes, textAppearanceResIndices);

        // Then, safely retrieve the styled attribute information.
        return context.obtainStyledAttributes(set, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Safely retrieve styled attribute information in this Context's theme using {@link
     * android.support.v7.widget.TintTypedArray}, after checking whether the theme is compatible with
     * the component's given style.
     *
     * <p>Set a component's {@link R.attr#enforceMaterialTheme enforceMaterialTheme} attribute to
     * <code>true</code> to ensure that the Context's theme inherits from {@link
     * R.style#Theme_MaterialComponents Theme.MaterialComponents}. For example, you'll want to do this
     * if the component uses a new attribute defined in <code>Theme.MaterialComponents</code> like
     * {@link R.attr#colorSecondary colorSecondary}.
     *
     * <p>New components should prefer to use {@link #obtainStyledAttributes(Context, AttributeSet,
     * int[], int, int, int...)}, and use {@link com.google.android.material.resources.MaterialResources}
     * as a replacement for the functionality in {@link android.support.v7.widget.TintTypedArray}.
     *
     * <p>If {@link R.attr#enforceTextAppearance} attribute is set to <code>true</code> and
     * textAppearanceResIndices parameter is specified and has non-negative values, this will also
     * check that a valid TextAppearance is set on this component for the text appearance resources
     * passed in.
     */
    @SuppressLint("RestrictedApi")
    public static TintTypedArray obtainTintedStyledAttributes(
            Context context,
            AttributeSet set,
            @StyleableRes int[] attrs,
            @AttrRes int defStyleAttr,
            @StyleRes int defStyleRes,
            @StyleableRes int... textAppearanceResIndices) {

        // First, check for a compatible theme.
        checkCompatibleTheme(context, set, defStyleAttr, defStyleRes);

        // Then, check that a textAppearance is set if enforceTextAppearance attribute is true
        checkTextAppearance(context, set, attrs, defStyleAttr, defStyleRes, textAppearanceResIndices);

        // Then, safely retrieve the styled attribute information.
        return TintTypedArray.obtainStyledAttributes(context, set, attrs, defStyleAttr, defStyleRes);
    }

    private static void checkCompatibleTheme(
            Context context, AttributeSet set, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        TypedArray a =
                context.obtainStyledAttributes(
                        set, R.styleable.ThemeEnforcement, defStyleAttr, defStyleRes);
        boolean enforceMaterialTheme =
                a.getBoolean(R.styleable.ThemeEnforcement_enforceMaterialTheme, false);
        a.recycle();

        if (enforceMaterialTheme) {
            TypedValue isMaterialTheme = new TypedValue();
            boolean resolvedValue =
                    context.getTheme().resolveAttribute(R.attr.isMaterialTheme, isMaterialTheme, true);

            if (!resolvedValue
                    || (isMaterialTheme.type == TypedValue.TYPE_INT_BOOLEAN && isMaterialTheme.data == 0)) {
                // If we were unable to resolve isMaterialTheme boolean attribute, or isMaterialTheme is
                // false, check for Material Theme color attributes
                checkMaterialTheme(context);
            }
        }
        checkAppCompatTheme(context);
    }

    private static void checkTextAppearance(
            Context context,
            AttributeSet set,
            @StyleableRes int[] attrs,
            @AttrRes int defStyleAttr,
            @StyleRes int defStyleRes,
            @StyleableRes int... textAppearanceResIndices) {
        TypedArray themeEnforcementAttrs =
                context.obtainStyledAttributes(
                        set, R.styleable.ThemeEnforcement, defStyleAttr, defStyleRes);
        boolean enforceTextAppearance =
                themeEnforcementAttrs.getBoolean(R.styleable.ThemeEnforcement_enforceTextAppearance, false);

        if (!enforceTextAppearance) {
            themeEnforcementAttrs.recycle();
            return;
        }

        boolean validTextAppearance;

        if (textAppearanceResIndices == null || textAppearanceResIndices.length == 0) {
            // No custom TextAppearance attributes passed in, check android:textAppearance
            validTextAppearance =
                    themeEnforcementAttrs.getResourceId(
                            R.styleable.ThemeEnforcement_android_textAppearance, -1)
                            != -1;
        } else {
            // Check custom TextAppearances are valid
            validTextAppearance =
                    isCustomTextAppearanceValid(
                            context, set, attrs, defStyleAttr, defStyleRes, textAppearanceResIndices);
        }

        themeEnforcementAttrs.recycle();

        if (!validTextAppearance) {
            throw new IllegalArgumentException(
                    "This component requires that you specify a valid TextAppearance attribute. Update your "
                            + "app theme to inherit from Theme.MaterialComponents (or a descendant).");
        }
    }

    private static boolean isCustomTextAppearanceValid(
            Context context,
            AttributeSet set,
            @StyleableRes int[] attrs,
            @AttrRes int defStyleAttr,
            @StyleRes int defStyleRes,
            @StyleableRes int... textAppearanceResIndices) {
        TypedArray componentAttrs =
                context.obtainStyledAttributes(set, attrs, defStyleAttr, defStyleRes);
        for (int customTextAppearanceIndex : textAppearanceResIndices) {
            if (componentAttrs.getResourceId(customTextAppearanceIndex, -1) == -1) {
                componentAttrs.recycle();
                return false;
            }
        }
        componentAttrs.recycle();
        return true;
    }

    public static void checkAppCompatTheme(Context context) {
        checkTheme(context, APPCOMPAT_CHECK_ATTRS, APPCOMPAT_THEME_NAME);
    }

    public static void checkMaterialTheme(Context context) {
        checkTheme(context, MATERIAL_CHECK_ATTRS, MATERIAL_THEME_NAME);
    }

    public static boolean isAppCompatTheme(Context context) {
        return isTheme(context, APPCOMPAT_CHECK_ATTRS);
    }

    public static boolean isMaterialTheme(Context context) {
        return isTheme(context, MATERIAL_CHECK_ATTRS);
    }

    private static boolean isTheme(Context context, int[] themeAttributes) {
        TypedArray a = context.obtainStyledAttributes(themeAttributes);
        for (int i = 0; i < themeAttributes.length; i++) {
            if (!a.hasValue(i)) {
                a.recycle();
                return false;
            }
        }
        a.recycle();
        return true;
    }

    private static void checkTheme(Context context, int[] themeAttributes, String themeName) {
        if (!isTheme(context, themeAttributes)) {
            throw new IllegalArgumentException(
                    "The style on this component requires your app theme to be "
                            + themeName
                            + " (or a descendant).");
        }
    }

    /**
     * Uses the materialThemeOverlay attribute to create a themed context. This allows us to use
     * ThemeOverlays with a default style, and gives us some protection against losing our
     * ThemeOverlay by clients who set android:theme or app:theme. If android:theme or app:theme is
     * specified by the client, any attributes defined there will take precedence over attributes
     * defined in materialThemeOverlay.
     */
    public static Context createThemedContext(
            Context context, AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        int materialThemeOverlayId =
                obtainMaterialThemeOverlayId(context, attrs, defStyleAttr, defStyleRes);
        if (materialThemeOverlayId != 0
                && (!(context instanceof ContextThemeWrapper)
                || ((ContextThemeWrapper) context).getThemeResId() != materialThemeOverlayId)) {
            // If the context isn't a ContextThemeWrapper, or it is but does not have the same theme as we
            // need, wrap it in a new wrapper.
            context = new ContextThemeWrapper(context, materialThemeOverlayId);

            // We want values set in android:theme or app:theme to always override values supplied by
            // materialThemeOverlay, so we'll wrap the context again if either of those are set.
            int androidThemeOverlayId = obtainAndroidThemeOverlayId(context, attrs);
            if (androidThemeOverlayId != 0) {
                context = new ContextThemeWrapper(context, androidThemeOverlayId);
            }
        }
        return context;
    }

    /**
     * Retrieves the value of {@code android:theme} or {@code app:theme}, not taking into account
     * {@code defStyleAttr} and {@code defStyleRes} because the Android theme overlays shouldn't work
     * from default styles.
     */
    @StyleRes
    private static int obtainAndroidThemeOverlayId(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, ANDROID_THEME_OVERLAY_ATTRS);
        int androidThemeId = a.getResourceId(0 /* index */, 0 /* defaultVal */);
        int appThemeId = a.getResourceId(1 /* index */, 0 /* defaultVal */);
        a.recycle();
        if (androidThemeId != 0) {
            return androidThemeId;
        } else {
            return appThemeId;
        }
    }

    /**
     * Retrieves the value of {@code materialThemeOverlay}, taking into account {@code defStyleAttr}
     * and {@code defStyleRes} because the Material theme overlay should work from default styles.
     */
    @StyleRes
    private static int obtainMaterialThemeOverlayId(
            Context context, AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        TypedArray a =
                context.obtainStyledAttributes(
                        attrs, MATERIAL_THEME_OVERLAY_ATTR, defStyleAttr, defStyleRes);
        int materialThemeOverlayId = a.getResourceId(0 /* index */, 0 /* defaultVal */);
        a.recycle();
        return materialThemeOverlayId;
    }
}

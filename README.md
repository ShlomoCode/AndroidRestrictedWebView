# Android Restricted WebView

The Android Restricted WebView is an Android application template that allows you to display a specific website using a WebView while restricting access to other sites. If the application attempts to load a URL other than the allowed site, an error message "This URL is not allowed" will be displayed.

## NetFree Support

**This app is configured to work ONLY with NetFree internet filtering.**

The app implements strict security measures:
- ✅ **HTTPS-Only**: All HTTP traffic is blocked
- ✅ **Certificate Pinning**: Only trusts NetFree CA certificates
- ✅ **No System Certificates**: Built-in Android certificates are not trusted

### Setting Up NetFree Certificates

**Quick Start: [📋 NETFREE_SETUP.md](./NETFREE_SETUP.md)** - Complete setup guide in Hebrew

The app is pre-configured for all 20 NetFree CA certificates. You only need to:
1. Rename certificate files (script provided: `rename_certificates.sh`)
2. Upload them to `app/src/main/res/raw/`
3. Build the app

For detailed instructions, see:
- [NETFREE_SETUP.md](./NETFREE_SETUP.md) - Step-by-step guide (Hebrew)
- [app/src/main/res/raw/README.md](./app/src/main/res/raw/README.md) - Technical details

## Configuration
In the file [`app/build.gradle`](./app/build.gradle) (editing the file or using env variables).

recompile and redeploy the application for the change to take effect.

## License

This application is distributed under the GNU General Public License version 3 (GNU GPL-3.0). See the [LICENSE](LICENSE) file for more details.

## Disclaimer

Please note that I am not a lawyer and the information provided here should not be considered legal advice. It is important to consult with a qualified legal professional to ensure compliance with all relevant licensing requirements and obligations.

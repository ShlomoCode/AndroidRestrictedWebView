# Android Restricted WebView

The Android Restricted WebView is an Android application template that allows you to display a specific website using a WebView while restricting access to other sites. If the application attempts to load a URL other than the allowed site, an error message "This URL is not allowed" will be displayed.

## NetFree Support

**This app is configured to work ONLY with NetFree internet filtering.**

The app implements strict security measures:
- ✅ **HTTPS-Only**: All HTTP traffic is blocked
- ✅ **Certificate Pinning**: Only trusts NetFree CA certificates
- ✅ **No System Certificates**: Built-in Android certificates are not trusted

### Setting Up NetFree Certificates

1. Obtain NetFree CA certificate files (`.crt` format)
2. Place them in: `app/src/main/res/raw/`
3. Reference them in: `app/src/main/res/xml/network_security_config.xml`

For detailed instructions, see: [app/src/main/res/raw/README.md](./app/src/main/res/raw/README.md)

## Configuration
In the file [`app/build.gradle`](./app/build.gradle) (editing the file or using env variables).

recompile and redeploy the application for the change to take effect.

## License

This application is distributed under the GNU General Public License version 3 (GNU GPL-3.0). See the [LICENSE](LICENSE) file for more details.

## Disclaimer

Please note that I am not a lawyer and the information provided here should not be considered legal advice. It is important to consult with a qualified legal professional to ensure compliance with all relevant licensing requirements and obligations.

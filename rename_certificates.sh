#!/bin/bash
# Script to rename NetFree certificate files for Android compatibility
# Usage: Place all .crt files in a folder, cd to that folder, then run this script

echo "NetFree Certificate Renamer"
echo "==========================="
echo ""
echo "This script will rename certificate files to be Android-compatible"
echo "(lowercase, underscores instead of hyphens, no leading numbers)"
echo ""

# Create rename function
rename_file() {
    local old_name="$1"
    local new_name="$2"

    if [ -f "$old_name" ]; then
        mv "$old_name" "$new_name"
        echo "✓ Renamed: $old_name → $new_name"
    else
        echo "⚠ Not found: $old_name (skipping)"
    fi
}

# Rename files that start with numbers
rename_file "018.crt" "netfree_018.crt"
rename_file "019.crt" "netfree_019.crt"
rename_file "099.crt" "netfree_099.crt"

# Rename files with hyphens to underscores
rename_file "ib-itc.crt" "ib_itc.crt"
rename_file "ib-partner.crt" "ib_partner.crt"
rename_file "ib-spotnet.crt" "ib_spotnet.crt"
rename_file "hadran-vpn.crt" "hadran_vpn.crt"
rename_file "kosher-sim-cellcom.crt" "kosher_sim_cellcom.crt"
rename_file "kosher-sim.crt" "kosher_sim.crt"
rename_file "ksim-itc.crt" "ksim_itc.crt"
rename_file "ksim-partner.crt" "ksim_partner.crt"
rename_file "ksim-pele.crt" "ksim_pele.crt"
rename_file "netfree-anywhere.crt" "netfree_anywhere.crt"
rename_file "sim-kasher-triple-c.crt" "sim_kasher_triple_c.crt"

echo ""
echo "==========================="
echo "Renaming complete!"
echo ""
echo "Files that don't need renaming:"
echo "  - amitnet.crt"
echo "  - bezeq.crt"
echo "  - hot.crt"
echo "  - itc.crt"
echo "  - x2one.crt"
echo "  - yossi.crt"
echo ""
echo "Next steps:"
echo "1. Copy ALL .crt files from this directory"
echo "2. Paste them into: app/src/main/res/raw/"
echo "3. Build the app: ./gradlew assembleDebug"
echo ""

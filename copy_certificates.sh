#!/bin/bash
# Copy and rename NetFree certificates to Android res/raw directory
# Usage: ./copy_certificates.sh /path/to/nf-certs

SOURCE_DIR="$1"
DEST_DIR="/home/user/AndroidRestrictedWebView/app/src/main/res/raw"

if [ -z "$SOURCE_DIR" ]; then
    echo "Usage: $0 /path/to/nf-certs"
    exit 1
fi

if [ ! -d "$SOURCE_DIR" ]; then
    echo "Error: Directory $SOURCE_DIR not found"
    exit 1
fi

echo "Copying and renaming certificates..."

# Files that start with numbers - add netfree_ prefix
cp "$SOURCE_DIR/018.crt" "$DEST_DIR/netfree_018.crt" 2>/dev/null && echo "✓ 018.crt → netfree_018.crt"
cp "$SOURCE_DIR/019.crt" "$DEST_DIR/netfree_019.crt" 2>/dev/null && echo "✓ 019.crt → netfree_019.crt"
cp "$SOURCE_DIR/099.crt" "$DEST_DIR/netfree_099.crt" 2>/dev/null && echo "✓ 099.crt → netfree_099.crt"

# Files with hyphens - replace with underscores
cp "$SOURCE_DIR/hadran-vpn.crt" "$DEST_DIR/hadran_vpn.crt" 2>/dev/null && echo "✓ hadran-vpn.crt → hadran_vpn.crt"
cp "$SOURCE_DIR/ib-itc.crt" "$DEST_DIR/ib_itc.crt" 2>/dev/null && echo "✓ ib-itc.crt → ib_itc.crt"
cp "$SOURCE_DIR/ib-partner.crt" "$DEST_DIR/ib_partner.crt" 2>/dev/null && echo "✓ ib-partner.crt → ib_partner.crt"
cp "$SOURCE_DIR/ib-spotnet.crt" "$DEST_DIR/ib_spotnet.crt" 2>/dev/null && echo "✓ ib-spotnet.crt → ib_spotnet.crt"
cp "$SOURCE_DIR/kosher-sim-cellcom.crt" "$DEST_DIR/kosher_sim_cellcom.crt" 2>/dev/null && echo "✓ kosher-sim-cellcom.crt → kosher_sim_cellcom.crt"
cp "$SOURCE_DIR/kosher-sim.crt" "$DEST_DIR/kosher_sim.crt" 2>/dev/null && echo "✓ kosher-sim.crt → kosher_sim.crt"
cp "$SOURCE_DIR/ksim-itc.crt" "$DEST_DIR/ksim_itc.crt" 2>/dev/null && echo "✓ ksim-itc.crt → ksim_itc.crt"
cp "$SOURCE_DIR/ksim-partner.crt" "$DEST_DIR/ksim_partner.crt" 2>/dev/null && echo "✓ ksim-partner.crt → ksim_partner.crt"
cp "$SOURCE_DIR/ksim-pele.crt" "$DEST_DIR/ksim_pele.crt" 2>/dev/null && echo "✓ ksim-pele.crt → ksim_pele.crt"
cp "$SOURCE_DIR/netfree-anywhere.crt" "$DEST_DIR/netfree_anywhere.crt" 2>/dev/null && echo "✓ netfree-anywhere.crt → netfree_anywhere.crt"
cp "$SOURCE_DIR/sim-kasher-triple-c.crt" "$DEST_DIR/sim_kasher_triple_c.crt" 2>/dev/null && echo "✓ sim-kasher-triple-c.crt → sim_kasher_triple_c.crt"

# Files that don't need renaming
cp "$SOURCE_DIR/amitnet.crt" "$DEST_DIR/amitnet.crt" 2>/dev/null && echo "✓ amitnet.crt"
cp "$SOURCE_DIR/bezeq.crt" "$DEST_DIR/bezeq.crt" 2>/dev/null && echo "✓ bezeq.crt"
cp "$SOURCE_DIR/hot.crt" "$DEST_DIR/hot.crt" 2>/dev/null && echo "✓ hot.crt"
cp "$SOURCE_DIR/itc.crt" "$DEST_DIR/itc.crt" 2>/dev/null && echo "✓ itc.crt"
cp "$SOURCE_DIR/x2one.crt" "$DEST_DIR/x2one.crt" 2>/dev/null && echo "✓ x2one.crt"
cp "$SOURCE_DIR/yossi.crt" "$DEST_DIR/yossi.crt" 2>/dev/null && echo "✓ yossi.crt"

echo ""
echo "Done! $(ls -1 $DEST_DIR/*.crt 2>/dev/null | wc -l) certificates copied."

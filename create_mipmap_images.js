const { Jimp } = require('jimp');

const sizes = {
    xxxhdpi: 192,
    xxhdpi: 144,
    xhdpi: 96,
    hdpi: 72,
    mdpi: 48,
    ldpi: 36,
};

async function main(imageUrl) {
    const image = await Jimp.read(imageUrl);
    image.resize(512, 512).write('app/src/main/ic_launcher-playstore.png');
    for (const [sizeName, sizePixels] of Object.entries(sizes)) {
        const launcherPath = `app/src/main/res/mipmap-${sizeName}/ic_launcher.png`;
        const roundPath = `app/src/main/res/mipmap-${sizeName}/ic_launcher_round.png`;
        image.resize(sizePixels, sizePixels).write(launcherPath);
        image.circle().write(roundPath);
    }
}

if (require.main === module) {
    const arg = process.argv[2];
    if (!arg) throw new Error('missing argument');
    main(arg);
}

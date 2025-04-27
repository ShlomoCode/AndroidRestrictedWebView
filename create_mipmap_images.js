const { Jimp } = require('jimp');
const fs = require('fs');

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
    image.resize({ w: 512, h: 512 }).write('app/src/main/ic_launcher-playstore.png');
    for (const [sizeName, sizePixels] of Object.entries(sizes)) {
        const mipmapFolder = `app/src/main/res/mipmap-${sizeName}`;
        if (!fs.existsSync(mipmapFolder)) {
            fs.mkdirSync(mipmapFolder, { recursive: true });
        }
        const launcherPath = `app/src/main/res/mipmap-${sizeName}/ic_launcher.png`;
        const roundPath = `app/src/main/res/mipmap-${sizeName}/ic_launcher_round.png`;
        image.resize({ w: sizePixels, h: sizePixels }).write(launcherPath);
        image.circle().write(roundPath);
    }
}
if (require.main === module) {
    const arg = process.argv[2];
    if (!arg) throw new Error('missing argument');
    main(arg);
}

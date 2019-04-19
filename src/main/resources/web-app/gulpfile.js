var gulp = require('gulp');
var concat = require('gulp-concat');
var concatCss = require('gulp-concat-css');
var removeUseStrict = require('gulp-remove-use-strict');

var paths = { // paths to resources used by gulp
    scripts: {
        src: [
            './app/**/*.module.js',
            './app/**/*.config.js',
            './app/**/*.service.js',
            './app/**/*.controller.js'
        ],
        dest: 'app/dist/'
    },
    css: {
        src: './app/assets/css/**/*.css',
        dest: 'app/dist/'
    }
};

function scripts() { // combines all app scripts into one file
    return gulp.src(paths.scripts.src)
        .pipe(concat('app.glob.js'))
        .pipe(removeUseStrict())
        .pipe(gulp.dest(paths.scripts.dest));
}

gulp.task('scripts', scripts);

function stylesheets() { // combines all the css into one file
    return gulp.src(paths.css.src)
        .pipe(concatCss('app.glob.css'))
        .pipe(gulp.dest(paths.scripts.dest));
}

gulp.task('stylesheets', stylesheets);

function watch() {
    gulp.watch(paths.scripts.src, 'scripts');
    gulp.watch(paths.css.src, stylesheets);
}

gulp.task('watch', watch);

gulp.task('build', gulp.parallel(['scripts', 'stylesheets']));

gulp.task('default', gulp.series('build'));

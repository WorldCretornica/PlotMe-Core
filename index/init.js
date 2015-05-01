(function() {

	skel.init({
		reset: 'full',
		breakpoints: {
			'global': { range: '*', href: 'index/style.css', viewport: { scalable: false } },
		}
	});
			window.onload = function() {
				document.body.className = '';
			}

			window.ontouchmove = function() {
				return false;
			}

			window.onorientationchange = function() {
				document.body.scrollTop = 0;
			}

})();
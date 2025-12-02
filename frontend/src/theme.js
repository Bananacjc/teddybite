import { extendTheme } from '@chakra-ui/react';

const theme = extendTheme({
  colors: {
    brand: {
      50: '#FFF8E1',
      100: '#FFECB3',
      200: '#FFE082',
      300: '#FFD54F',
      400: '#FFCA28',
      500: '#FFC107', // Main Brand Color (Yellow)
      600: '#FFB300',
      700: '#FFA000',
      800: '#FF8F00',
      900: '#FF6F00',
    },
    brown: {
      50: '#EFEBE9',
      100: '#D7CCC8',
      200: '#BCAAA4',
      300: '#A1887F',
      400: '#8D6E63',
      500: '#795548',
      600: '#6D4C41',
      700: '#5D4037',
      800: '#4E342E',
      900: '#3E2723', // Dark Brown
    },
  },
  styles: {
    global: {
      body: {
        bg: '#FDFBF7',
        color: 'brown.900',
      },
    },
  },
  fonts: {
    heading: `'Poppins', sans-serif`,
    body: `'Poppins', sans-serif`,
  },
  components: {
    Button: {
      baseStyle: {
        fontWeight: 'bold',
        borderRadius: 'xl',
      },
    },
  },
});

export default theme;

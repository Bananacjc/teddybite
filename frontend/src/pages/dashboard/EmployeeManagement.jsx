import React from 'react';
import { Box, Heading, SimpleGrid, Card, CardBody, Avatar, Text, Badge, HStack, Button } from '@chakra-ui/react';
import { AddIcon } from '@chakra-ui/icons';

const EmployeeManagement = () => {
  return (
    <Box>
      <HStack justify="space-between" mb={6}>
        <Heading size="lg" color="brown.900">Employee Management</Heading>
        <Button leftIcon={<AddIcon />} colorScheme="brand" bg="brand.500" color="brown.900">
          Add Employee
        </Button>
      </HStack>

      <SimpleGrid columns={{ base: 1, md: 2, lg: 3 }} spacing={6}>
        {[1, 2, 3].map((i) => (
          <Card key={i} borderRadius="xl" boxShadow="sm">
            <CardBody>
              <HStack spacing={4} align="start">
                <Avatar name={`Employee ${i}`} bg="brown.200" />
                <Box>
                  <Heading size="sm">Employee Name {i}</Heading>
                  <Text fontSize="sm" color="gray.500">Position</Text>
                  <Badge colorScheme="green" mt={2}>Active</Badge>
                </Box>
              </HStack>
              <Text mt={4} fontSize="sm" color="gray.600">
                ID: EMP-00{i}<br/>
                Joined: Jan 2024
              </Text>
              <HStack mt={4}>
                <Button size="sm" flex={1}>View Profile</Button>
                <Button size="sm" colorScheme="blue" variant="outline" flex={1}>Edit</Button>
              </HStack>
            </CardBody>
          </Card>
        ))}
      </SimpleGrid>
    </Box>
  );
};

export default EmployeeManagement;
